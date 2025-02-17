package ceui.lisa.fragments;

import android.content.Intent;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.ybq.android.spinkit.style.DoubleBounce;

import java.util.ArrayList;
import java.util.List;

import ceui.lisa.R;
import ceui.lisa.activities.TemplateFragmentActivity;
import ceui.lisa.adapters.PivisionHorizontalAdapter;
import ceui.lisa.databinding.FragmentPivisionHorizontalBinding;
import ceui.lisa.http.NullCtrl;
import ceui.lisa.http.Retro;
import ceui.lisa.model.ArticalResponse;
import ceui.lisa.model.SpotlightArticlesBean;
import ceui.lisa.utils.DensityUtil;
import ceui.lisa.view.LinearItemHorizontalDecoration;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

import static ceui.lisa.activities.Shaft.sUserModel;
import static ceui.lisa.fragments.FragmentList.animateDuration;

/**
 * Pivision 文章
 */
public class FragmentPivisionHorizontal extends BaseBindFragment<FragmentPivisionHorizontalBinding> {

    private List<SpotlightArticlesBean> allItems = new ArrayList<>();
    private PivisionHorizontalAdapter mAdapter;

    @Override
    void initLayout() {
        mLayoutID = R.layout.fragment_pivision_horizontal;
    }

    @Override
    void initData() {
        DoubleBounce doubleBounce = new DoubleBounce();
        doubleBounce.setColor(getResources().getColor(R.color.white));
        baseBind.progress.setIndeterminateDrawable(doubleBounce);
        LandingAnimator landingAnimator = new LandingAnimator(new AnticipateOvershootInterpolator());
        landingAnimator.setAddDuration(400L);
        landingAnimator.setRemoveDuration(animateDuration);
        landingAnimator.setMoveDuration(animateDuration);
        landingAnimator.setChangeDuration(animateDuration);
        baseBind.recyclerView.setItemAnimator(landingAnimator);
        baseBind.recyclerView.addItemDecoration(new LinearItemHorizontalDecoration(DensityUtil.dp2px(8.0f)));
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        baseBind.recyclerView.setLayoutManager(manager);
        baseBind.recyclerView.setHasFixedSize(true);
        mAdapter = new PivisionHorizontalAdapter(allItems, mContext);
        baseBind.recyclerView.setAdapter(mAdapter);
        baseBind.seeMore.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, TemplateFragmentActivity.class);
            intent.putExtra(TemplateFragmentActivity.EXTRA_FRAGMENT, "特辑");
            startActivity(intent);
        });
        getFirstData();
    }

    public void getFirstData() {
        Retro.getAppApi().getArticals(sUserModel.getResponse().getAccess_token())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NullCtrl<ArticalResponse>() {
                    @Override
                    public void success(ArticalResponse articalResponse) {
                        allItems.clear();
                        allItems.addAll(articalResponse.getList());
                        mAdapter.notifyItemRangeInserted(0, articalResponse.getList().size());
                    }

                    @Override
                    public void must(boolean isSuccess) {
                        baseBind.progress.setVisibility(View.INVISIBLE);
                    }
                });
    }
}
