package ceui.lisa.fragments;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import ceui.lisa.R;
import ceui.lisa.activities.TemplateFragmentActivity;
import ceui.lisa.adapters.ArticleAdapter;
import ceui.lisa.http.Retro;
import ceui.lisa.interfaces.OnItemClickListener;
import ceui.lisa.model.ArticalResponse;
import ceui.lisa.model.SpotlightArticlesBean;
import ceui.lisa.utils.DensityUtil;
import ceui.lisa.view.LinearItemDecoration;
import io.reactivex.Observable;

import static ceui.lisa.activities.Shaft.sUserModel;

public class FragmentPivision extends BaseListFragment<ArticalResponse, ArticleAdapter, SpotlightArticlesBean> {

    @Override
    Observable<ArticalResponse> initApi() {
        return Retro.getAppApi().getArticals(sUserModel.getResponse().getAccess_token());
    }

    @Override
    String getToolbarTitle() {
        return "PixiVision特辑";
    }

    @Override
    void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new LinearItemDecoration(DensityUtil.dp2px(16.0f)));
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    Observable<ArticalResponse> initNextApi() {
        return Retro.getAppApi().getNextArticals(sUserModel.getResponse().getAccess_token(), nextUrl);
    }

    @Override
    void initAdapter() {
        mAdapter = new ArticleAdapter(allItems, mContext);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, int viewType) {
                Intent intent = new Intent(mContext, TemplateFragmentActivity.class);
                intent.putExtra(TemplateFragmentActivity.EXTRA_FRAGMENT, "网页链接");
                intent.putExtra("url", allItems.get(position).getArticle_url());
                intent.putExtra("title", getString(R.string.pixiv_special));
                startActivity(intent);
            }
        });
    }
}
