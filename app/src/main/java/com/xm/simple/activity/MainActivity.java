package com.xm.simple.activity;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.xm.simple.base.BaseActivity;
import com.xm.xdownload.utils.ToastUtils;
import com.xm.simple.R;
import com.xm.simple.databinding.ActivityMainBinding;
import com.xm.simple.fragment.DownloadFragment;
import com.xm.simple.fragment.HomeFragment;
import com.xm.simple.fragment.RequestFragment;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements NavigationView.OnNavigationItemSelectedListener {
    /* HashMap */
    private HashMap<String,Fragment> mFragmentHashMap = new HashMap<>();

    @Override
    public int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    public void initBinding(ActivityMainBinding binding) {}

    @Override
    public void initView(Bundle savedInstanceState) {
        /* 默认显示 首页 */
        switchFragment("网络请求");
        /* 监听事件 */
        mBinding.navigationView.setNavigationItemSelectedListener(this);
    }


    /** 获取抽屉菜单 */
    public DrawerLayout getDrawerLayout(){
        return mBinding.drawerlayout;
    }

    /** 抽屉菜单，文本选中的时候.切换Fragment */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        mBinding.drawerlayout.closeDrawers();
        String title = item.getTitle().toString();
        switchFragment(title);
        return true;
    }

    /** 切换Fragment -> (ps:正式写的时候，可别中文哦 -> 这仅仅为了开源着阅读) */
    private void switchFragment(String channel){
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        Fragment currentFragment = mFragmentHashMap.get(channel);
        switch (channel){
            case "更新":
                if(currentFragment == null){
                    currentFragment = new HomeFragment();
                    rememberTargetFragment(channel,fragmentTransaction,currentFragment);
                }
                break;
            case "下载":
                if(currentFragment == null){
                    currentFragment = new DownloadFragment();
                    rememberTargetFragment(channel,fragmentTransaction,currentFragment);
                }
                break;
            case "网络请求":
                if(currentFragment == null){
                    currentFragment = new RequestFragment();
                    rememberTargetFragment(channel,fragmentTransaction,currentFragment);
                }
                break;
            default:
                ToastUtils.getInstance().toast("暂未开发.");
                return;
        }
        //显示目标Fragment,隐藏其他Fragment
        showTargetFragment(fragmentTransaction,currentFragment);
    }

    /**
     * 记录Fragment。并添加到 Fragment事务
     * @param channel                   标题
     * @param fragmentTransaction       Framgnet事务
     * @param currentFragment           保留，也就是不隐藏的Fragment
     */
    private void rememberTargetFragment(String channel,FragmentTransaction fragmentTransaction,Fragment currentFragment){
        mFragmentHashMap.put(channel,currentFragment);
        fragmentTransaction.add(R.id.fm_content,currentFragment);
    }

    /**
     * 显示指定的Fragment,并隐藏其他的
     * @param fragmentTransaction       Framgnet事务
     * @param currentFragment           保留，也就是不隐藏的Fragment
     */
    private void showTargetFragment(FragmentTransaction fragmentTransaction,Fragment currentFragment){
        Collection<Fragment> values = mFragmentHashMap.values();
        Iterator<Fragment> iterator = values.iterator();
        while (iterator.hasNext()){
            Fragment value = iterator.next();
            if(value == currentFragment){
                fragmentTransaction.show(value);
            }else{
                fragmentTransaction.hide(value);
            }
        }
        //提交所有事务
        fragmentTransaction.commitAllowingStateLoss();
    }
}
