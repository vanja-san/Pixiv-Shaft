package ceui.lisa.fragments

import android.content.Intent
import ceui.lisa.R
import ceui.lisa.activities.TemplateFragmentActivity
import ceui.lisa.databinding.FragmentAboutBinding

class FragmentAbout : BaseBindFragment<FragmentAboutBinding>() {

    internal override fun initLayout() {
        mLayoutID = R.layout.fragment_about
    }

    internal override fun initData() {
        baseBind.toolbar.setNavigationOnClickListener { mActivity.finish() }
        baseBind.pixivProblem.setOnClickListener {
            val intent = Intent(mContext, TemplateFragmentActivity::class.java)
            intent.putExtra(TemplateFragmentActivity.EXTRA_FRAGMENT, "网页链接")
            intent.putExtra("url", "https://app.pixiv.help/hc/zh-cn")
            intent.putExtra("title", "常见问题")
            startActivity(intent)
        }
        baseBind.pixivUseDetail.setOnClickListener {
            val intent = Intent(mContext, TemplateFragmentActivity::class.java)
            intent.putExtra(TemplateFragmentActivity.EXTRA_FRAGMENT, "网页链接")
            intent.putExtra("url", "https://www.pixiv.net/terms/?page=term&appname=pixiv_ios")
            intent.putExtra("title", "服务条款")
            startActivity(intent)
        }
        baseBind.pixivPrivacy.setOnClickListener {
            val intent = Intent(mContext, TemplateFragmentActivity::class.java)
            intent.putExtra(TemplateFragmentActivity.EXTRA_FRAGMENT, "网页链接")
            intent.putExtra("url", "https://www.pixiv.net/terms/?page=privacy&appname=pixiv_ios")
            intent.putExtra("title", "隐私政策")
            startActivity(intent)
        }
        baseBind.projectWebsite.setOnClickListener {
            val intent = Intent(mContext, TemplateFragmentActivity::class.java)
            intent.putExtra(TemplateFragmentActivity.EXTRA_FRAGMENT, "网页链接")
            intent.putExtra("url", "https://github.com/CeuiLiSA/Pixiv-Shaft")
            intent.putExtra("title", "项目主页")
            startActivity(intent)
        }
        baseBind.projectLicense.setOnClickListener {
            val intent = Intent(mContext, TemplateFragmentActivity::class.java)
            intent.putExtra(TemplateFragmentActivity.EXTRA_FRAGMENT, "License")
            startActivity(intent)
        }
    }
}
