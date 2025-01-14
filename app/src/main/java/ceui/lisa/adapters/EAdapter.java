package ceui.lisa.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import ceui.lisa.R;
import ceui.lisa.databinding.RecyUserEventBinding;
import ceui.lisa.model.IllustsBean;
import ceui.lisa.utils.GlideUtil;

public class EAdapter extends BaseAdapter<IllustsBean, RecyUserEventBinding> {

    private int imageSize;

    public EAdapter(List<IllustsBean> targetList, Context context) {
        super(targetList, context);
        imageSize = mContext.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public void initLayout() {
        mLayoutID = R.layout.recy_user_event;
    }

    @Override
    public void bindData(IllustsBean target, ViewHolder<RecyUserEventBinding> bindView, int position) {
        ViewGroup.LayoutParams params = bindView.baseBind.illustImage.getLayoutParams();
        params.height = imageSize * 2 / 3;
        params.width = imageSize;
        bindView.baseBind.illustImage.setLayoutParams(params);
        bindView.baseBind.userName.setText(allIllust.get(position).getUser().getName());
        bindView.baseBind.star.setText(allIllust.get(position).isIs_bookmarked() ? "取消收藏" : "收藏");
        if (!TextUtils.isEmpty(allIllust.get(position).getCreate_date())) {
            bindView.baseBind.illustDate.setText(allIllust.get(position).getCreate_date().substring(0, 16));
        }

        Glide.with(mContext).load(GlideUtil.getMediumImg(allIllust.get(position)
                .getUser().getProfile_image_urls().getMedium())).into(bindView.baseBind.userHead);
        Glide.with(mContext).load(GlideUtil.getLargeImage(allIllust.get(position)))
                .placeholder(R.color.light_bg)
                .into(bindView.baseBind.illustImage);
        if (mOnItemClickListener != null) {
            bindView.itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(v, position, 0));
            bindView.baseBind.userHead.setOnClickListener(v -> mOnItemClickListener.onItemClick(v, position, 1));
            bindView.baseBind.download.setOnClickListener(v -> mOnItemClickListener.onItemClick(v, position, 2));
            bindView.baseBind.star.setOnClickListener(v -> {
                bindView.baseBind.star.setText(allIllust.get(position).isIs_bookmarked() ? "收藏" : "取消收藏");
                mOnItemClickListener.onItemClick(v, position, 3);
            });
        }
    }
}
