package com.example.shumetheny.ticket_booking.fragment


import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.example.shumetheny.ticket_booking.R
import com.example.shumetheny.ticket_booking.activity.TimeTableActivity
import com.example.shumetheny.ticket_booking.activity.UsersActivity
import com.example.shumetheny.ticket_booking.bean.Movie
import com.example.shumetheny.ticket_booking.bean.getItemView
import com.example.shumetheny.ticket_booking.widget.CommonFilterPop
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_blank.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class BlankFragment : Fragment() {

    var mContext:Context? = null

    val gson by lazy { Gson() }
    val httpClient by lazy { OkHttpClient()}

    inner class CinemaListViewOnClickListener:AdapterView.OnItemClickListener{
        override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            val movie = movieList[p2]
            startActivityForResult(Intent().apply { setClass(activity,TimeTableActivity::class.java);putExtra("movie",Gson().toJson(movie))},2)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==2){
            if(resultCode==3){
                activity.findViewById<ViewPager>(R.id.viewpager_main).currentItem=1
                (activity as? UsersActivity)?.orderFragment?.LoadOrderask()?.execute()
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext=context
    }


    inner class LoadMovieask: AsyncTask<Any, Any, Array<Movie>>(){
        override fun doInBackground(vararg p0: Any?): Array<Movie> {
            val result = httpClient.newCall(Request.Builder().url("http://192.168.105.57:8080/CinemaSystem/API?action=movieList").build()).execute().body()!!.string()
            val movies = gson.fromJson(result, Array<com.example.shumetheny.ticket_booking.bean.Movie>::class.java)
            return movies;
        }

        override fun onPostExecute(result: Array<Movie>) {
            movieList.clear()
            movieList.addAll(result)
            listview_cinema.adapter =object:BaseAdapter(){
                override fun getView(p0: Int, p1: View?, p2: ViewGroup?):View =if(p1!=null)p1 else movieList[p0].getItemView(layoutInflater)

                override fun getItem(p0: Int)= movieList[p0]

                override fun getItemId(p0: Int)=0L

                override fun getCount(): Int = movieList.size
            }
        }
    }

    private var mPopupWindow: CommonFilterPop? = null

    /**
     * 展示电影的数据源
     */
    var mPopBeens: List<String> = listOf("电影")
    /**
     * 展示类型的数据
     */
    var mTypes: List<String> = listOf("类型","搞笑","爱情")
    /**
     * 展示时间的数据
     */
    var mTimes: List<String> = ArrayList()
    /**
     * 展示的时间str集合
     */
    var mTimeStr: List<String> = ArrayList()

    val movieList = mutableListOf(Movie("暂无数据"))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_blank,container,false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // cb1操作
        // 点击选择城市整体
        ll_name_tab.setOnClickListener(View.OnClickListener {
            if (cb_name.isChecked())
                cb_name.setChecked(false)
            else
                cb_name.setChecked(true)
        })
        // 点击选择类型整体
        ll_type.setOnClickListener(View.OnClickListener {
            if (cb_type.isChecked())
                cb_type.setChecked(false)
            else
                cb_type.setChecked(true)
        })
        // 点击选择时间整体
        ll_score.setOnClickListener(View.OnClickListener {
            if (cb_score.isChecked())
                cb_score.setChecked(false)
            else
                cb_score.setChecked(true)
        })
        // 选择城市cb
        cb_name.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            filterTabToggle(isChecked, ll_name_tab, mPopBeens, AdapterView.OnItemClickListener { adapterView, view, position, l ->
                hidePopListView()
                cb_name.setText(mPopBeens[position])
            }, cb_name, cb_type, cb_score)
        })

        // 选择类型cb
        cb_type.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            filterTabToggle(isChecked, ll_type, mTypes, AdapterView.OnItemClickListener { adapterView, view, position, l ->
                hidePopListView()
                cb_type.setText(mTypes[position])
            }, cb_type, cb_name, cb_score)
        })
        // 选择时间cb
        cb_score.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            filterTabToggle(isChecked, ll_score, mTimeStr, AdapterView.OnItemClickListener { adapterView, view, position, l ->
                hidePopListView()
                cb_score.setText(mTimeStr[position])
            }, cb_score, cb_name, cb_type)
        })
        listview_cinema.onItemClickListener=(CinemaListViewOnClickListener())
        listview_cinema.adapter = object:BaseAdapter(){
            override fun getView(p0: Int, p1: View?, p2: ViewGroup?):View =if(p1!=null)p1 else movieList[p0].getItemView(layoutInflater)

            override fun getItem(p0: Int)= movieList[p0]

            override fun getItemId(p0: Int)=0L

            override fun getCount(): Int = movieList.size
        }
        LoadMovieask().execute()
    }

    /**
     * 列表选择popupWindow
     *
     * @param parentView        父View
     * @param itemTexts         列表项文本集合
     * @param itemClickListener 列表项点击事件
     */
    fun showFilterPopupWindow(parentView: View,
                              itemTexts: List<String>,
                              itemClickListener: AdapterView.OnItemClickListener,
                              dismissListener: CustomerDismissListener) {
        showFilterPopupWindow(parentView, itemTexts, itemClickListener, dismissListener, 0f)
    }

    /**
     * 列表选择popupWindow
     *
     * @param parentView        父View
     * @param itemTexts         列表项文本集合
     * @param itemClickListener 列表项点击事件
     * @param alpha             背景透明度
     */
    fun showFilterPopupWindow(parentView: View,
                              itemTexts: List<String>,
                              itemClickListener: AdapterView.OnItemClickListener,
                              dismissListener: CustomerDismissListener, alpha: Float) {
        var alpha = alpha

        // 判断当前是否显示
        if (mPopupWindow != null && mPopupWindow!!.isShowing()) {
            mPopupWindow?.dismiss()
            mPopupWindow = null
        }
        mPopupWindow = CommonFilterPop(activity, itemTexts)
        mPopupWindow?.setOnDismissListener(dismissListener)
        // 绑定筛选点击事件
        mPopupWindow?.setOnItemSelectedListener(itemClickListener)
        // 如果透明度设置为0的话,则默认设置为0.6f
        if (0f == alpha) {
            alpha = 0.6f
        }
        // 设置背景透明度
        val lp = activity.window.attributes
        lp.alpha = alpha
        activity.window.attributes = lp
        // 显示pop
        mPopupWindow?.showAsDropDown(parentView)

    }

    /**
     * 自定义OnDismissListener
     */
    inner open class CustomerDismissListener : PopupWindow.OnDismissListener {
        override fun onDismiss() {
            // 当pop消失的时候,重置背景色透明度
            val lp = activity.window.attributes
            lp.alpha = 1.0f
            activity.window.attributes = lp
        }
    }

    /**
     * 隐藏pop
     */
    fun hidePopListView() {
        // 判断当前是否显示,如果显示则dismiss
        if (mPopupWindow != null && mPopupWindow!!.isShowing()) {
            mPopupWindow?.dismiss()
            mPopupWindow = null
        }
    }

    /**
     * Tab筛选栏切换
     *
     * @param isChecked         选中状态
     * @param showView          展示pop的跟布局
     * @param showMes           展示选择的数据
     * @param itemClickListener 点击回调
     * @param tabs              所有的cb(需要几个输入几个就可以,cb1,cb2....)
     */
    fun filterTabToggle(isChecked: Boolean, showView: View, showMes: List<String>, itemClickListener: AdapterView.OnItemClickListener, vararg tabs: CheckBox) {
        if (isChecked) {
            if (tabs.size <= 0) {
                return
            }
            // 第一个checkBox为当前点击选中的cb,其他cb进行setChecked(false);
            for (i in 1 until tabs.size) {
                tabs[i].setChecked(false)
            }

            showFilterPopupWindow(showView, showMes, itemClickListener, object : CustomerDismissListener() {
                override fun onDismiss() {
                    super.onDismiss()
                    // 当pop消失时对第一个cb进行.setChecked(false)操作
                    tabs[0].setChecked(false)
                }
            })
        } else {
            // 关闭checkBox时直接隐藏popuwindow
            hidePopListView()
        }
    }

}// Required empty public constructor

