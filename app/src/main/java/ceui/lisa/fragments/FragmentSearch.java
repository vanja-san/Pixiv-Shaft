package ceui.lisa.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ceui.lisa.R;
import ceui.lisa.activities.Shaft;
import ceui.lisa.activities.TemplateFragmentActivity;
import ceui.lisa.activities.UserDetailActivity;
import ceui.lisa.adapters.SearchHintAdapter;
import ceui.lisa.database.AppDatabase;
import ceui.lisa.database.SearchEntity;
import ceui.lisa.databinding.FragmentSearchBinding;
import ceui.lisa.http.ErrorCtrl;
import ceui.lisa.http.NullCtrl;
import ceui.lisa.http.Retro;
import ceui.lisa.interfaces.OnItemClickListener;
import ceui.lisa.model.TrendingtagResponse;
import ceui.lisa.utils.Common;
import ceui.lisa.utils.PixivOperate;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static ceui.lisa.activities.Shaft.sUserModel;
import static ceui.lisa.fragments.FragmentFilter.SEARCH_TYPE;
import static ceui.lisa.utils.Common.isNumeric;

public class FragmentSearch extends BaseBindFragment<FragmentSearchBinding> {

    private ObservableEmitter<String> fuck = null;
    private int searchType = 0;

    @Override
    void initLayout() {
        mLayoutID = R.layout.fragment_search;
    }

    @Override
    void initData() {
        ViewGroup.LayoutParams headParams = baseBind.head.getLayoutParams();
        headParams.height = Shaft.statusHeight;
        baseBind.head.setLayoutParams(headParams);
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                fuck = emitter;
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(800, TimeUnit.MILLISECONDS)
                .subscribe(new ErrorCtrl<String>() {
                    @Override
                    public void onNext(String s) {
                        completeWord(s);
                    }
                });
        baseBind.inputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String key = String.valueOf(charSequence);
                if (key.length() != 0 && searchType == 0) {
                    fuck.onNext(key);
                    baseBind.clear.setVisibility(View.VISIBLE);
                } else {
                    baseBind.hintList.setVisibility(View.GONE);
                    baseBind.clear.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        baseBind.inputBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (!TextUtils.isEmpty(baseBind.inputBox.getText().toString())) {
                    dispatchClick(baseBind.inputBox.getText().toString(), searchType);
                } else {
                    Common.showToast("请输入搜索内容");
                }
                return false;
            }
        });
        baseBind.inputBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (baseBind.hintList.getAdapter() != null) {
                        baseBind.hintList.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        baseBind.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseBind.inputBox.setText("");
            }
        });

        baseBind.inputBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (baseBind.hintList.getAdapter() != null &&
                        ((SearchHintAdapter) baseBind.hintList.getAdapter())
                                .getKeyword().equals(baseBind.inputBox.getText().toString())) {
                    baseBind.hintList.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        baseBind.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (baseBind.hintList.getVisibility() == View.VISIBLE) {
                    baseBind.hintList.setVisibility(View.INVISIBLE);
                }
            }
        });
        baseBind.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setItems(SEARCH_TYPE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (searchType != which) {
                            baseBind.inputBox.setText("");
                            baseBind.inputBox.setHint(SEARCH_TYPE[which]);
                            searchType = which;
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        baseBind.inputBox.setHint(SEARCH_TYPE[searchType]);
        getHotTags();
    }


    private void dispatchClick(String keyWord, int searchType) {
        if (searchType == 0) {
            insertSearchHistory(keyWord);
            baseBind.hintList.setVisibility(View.INVISIBLE);

            Intent intent = new Intent(mContext, TemplateFragmentActivity.class);
            intent.putExtra(TemplateFragmentActivity.EXTRA_KEYWORD, keyWord);
            intent.putExtra(TemplateFragmentActivity.EXTRA_FRAGMENT,
                    "搜索结果");
            startActivity(intent);
        } else if (searchType == 1) {
            if (isNumeric(keyWord)) {
                insertSearchHistory(keyWord);
                PixivOperate.getIllustByID(sUserModel, Integer.valueOf(keyWord), mContext);
            } else {
                Common.showToast("ID必须为全数字");
            }
        } else if (searchType == 2) {
            insertSearchHistory(keyWord);
            Intent intent = new Intent(mContext, TemplateFragmentActivity.class);
            intent.putExtra(TemplateFragmentActivity.EXTRA_KEYWORD,
                    keyWord);
            intent.putExtra(TemplateFragmentActivity.EXTRA_FRAGMENT,
                    "搜索用户");
            startActivity(intent);
        } else if (searchType == 3) {
            if (isNumeric(keyWord)) {
                insertSearchHistory(keyWord);
                Intent intent = new Intent(mContext, UserDetailActivity.class);
                intent.putExtra("user id", Integer.valueOf(keyWord));
                startActivity(intent);
            } else {
                Common.showToast("ID必须为全数字");
            }
        }
    }

    private void completeWord(String key) {
        Retro.getAppApi().searchCompleteWord(sUserModel.getResponse().getAccess_token(), key)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NullCtrl<TrendingtagResponse>() {
                    @Override
                    public void success(TrendingtagResponse trendingtagResponse) {
                        baseBind.hintList.setLayoutManager(new LinearLayoutManager(mContext));
                        SearchHintAdapter searchHintAdapter =
                                new SearchHintAdapter(trendingtagResponse.getList(), mContext, key);
                        searchHintAdapter.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position, int viewType) {
                                insertSearchHistory(trendingtagResponse.getList().get(position).getTag());
                                baseBind.hintList.setVisibility(View.INVISIBLE);

                                Intent intent = new Intent(mContext, TemplateFragmentActivity.class);
                                intent.putExtra(TemplateFragmentActivity.EXTRA_KEYWORD,
                                        trendingtagResponse.getList().get(position).getTag());
                                intent.putExtra(TemplateFragmentActivity.EXTRA_FRAGMENT,
                                        "搜索结果");
                                startActivity(intent);
                            }
                        });
                        baseBind.hintList.setAdapter(searchHintAdapter);
                        baseBind.hintList.setVisibility(View.VISIBLE);
                    }
                });
    }


    private void insertSearchHistory(String key) {
        SearchEntity searchEntity = new SearchEntity();
        searchEntity.setKeyword(key);
        searchEntity.setSearchType(searchType);
        searchEntity.setSearchTime(System.currentTimeMillis());
        searchEntity.setId(searchEntity.getKeyword().hashCode() + searchEntity.getSearchType());
        AppDatabase.getAppDatabase(Shaft.getContext()).searchDao().insert(searchEntity);
    }

    private void getHotTags() {
        Retro.getAppApi().getHotTags(sUserModel.getResponse().getAccess_token())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorCtrl<TrendingtagResponse>() {
                    @Override
                    public void onNext(TrendingtagResponse trendingtagResponse) {
                        if (trendingtagResponse != null) {
                            baseBind.hotTags.setAdapter(new TagAdapter<TrendingtagResponse.TrendTagsBean>(
                                    trendingtagResponse.getList().subList(0, 15)) {
                                @Override
                                public View getView(FlowLayout parent, int position, TrendingtagResponse.TrendTagsBean trendTagsBean) {
                                    TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.recy_single_line_text,
                                            parent, false);
                                    tv.setText(trendTagsBean.getTag());
                                    return tv;
                                }
                            });
                            baseBind.hotTags.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                                @Override
                                public boolean onTagClick(View view, int position, FlowLayout parent) {
                                    insertSearchHistory(trendingtagResponse.getList().get(position).getTag());
                                    baseBind.hintList.setVisibility(View.INVISIBLE);

                                    Intent intent = new Intent(mContext, TemplateFragmentActivity.class);
                                    intent.putExtra(TemplateFragmentActivity.EXTRA_KEYWORD,
                                            trendingtagResponse.getList().get(position).getTag());
                                    intent.putExtra(TemplateFragmentActivity.EXTRA_FRAGMENT,
                                            "搜索结果");
                                    startActivity(intent);
                                    return false;
                                }
                            });
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        List<SearchEntity> history = AppDatabase.getAppDatabase(Shaft.getContext()).searchDao().getAll(15);
        baseBind.searchHistory.setAdapter(new TagAdapter<SearchEntity>(history) {
            @Override
            public View getView(FlowLayout parent, int position, SearchEntity searchEntity) {
                TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.recy_single_line_text,
                        parent, false);
                tv.setText(searchEntity.getKeyword());
                return tv;
            }
        });
        baseBind.searchHistory.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (history.get(position).getSearchType() == 0) {
                    history.get(position).setSearchTime(System.currentTimeMillis());
                    AppDatabase.getAppDatabase(mContext).searchDao().insert(history.get(position));
                    baseBind.hintList.setVisibility(View.INVISIBLE);

                    Intent intent = new Intent(mContext, TemplateFragmentActivity.class);
                    intent.putExtra(TemplateFragmentActivity.EXTRA_KEYWORD, history.get(position).getKeyword());
                    intent.putExtra(TemplateFragmentActivity.EXTRA_FRAGMENT,
                            "搜索结果");
                    startActivity(intent);
                } else if (history.get(position).getSearchType() == 1) {
                    history.get(position).setSearchTime(System.currentTimeMillis());
                    AppDatabase.getAppDatabase(mContext).searchDao().insert(history.get(position));
                    PixivOperate.getIllustByID(sUserModel, Integer.valueOf(history.get(position).getKeyword()), mContext);
                } else if (history.get(position).getSearchType() == 2) {
                    history.get(position).setSearchTime(System.currentTimeMillis());
                    AppDatabase.getAppDatabase(mContext).searchDao().insert(history.get(position));
                    Intent intent = new Intent(mContext, TemplateFragmentActivity.class);
                    intent.putExtra(TemplateFragmentActivity.EXTRA_KEYWORD,
                            history.get(position).getKeyword());
                    intent.putExtra(TemplateFragmentActivity.EXTRA_FRAGMENT,
                            "搜索用户");
                    startActivity(intent);
                } else if (history.get(position).getSearchType() == 3) {
                    history.get(position).setSearchTime(System.currentTimeMillis());
                    AppDatabase.getAppDatabase(mContext).searchDao().insert(history.get(position));
                    Intent intent = new Intent(mContext, UserDetailActivity.class);
                    intent.putExtra("user id", Integer.valueOf(history.get(position).getKeyword()));
                    startActivity(intent);
                }
                return false;
            }
        });
    }
}
