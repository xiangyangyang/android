package com.chai.xiangyang.stickerheader.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chai.xiangyang.stickerheader.R
import com.chai.xiangyang.stickerheader.viewmodel.MainViewModel
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(){

    private val mainViewModel:MainViewModel by inject()
    private lateinit var recyclerView: StickerHeaderRecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView=findViewById<StickerHeaderRecyclerView>(R.id.book_recycler_view)
        recyclerView.setHasFixedSize(true)
        val viewAdapter = GridStickerHeaderAdapter(this,mainViewModel.getBookList())
        val viewManager = GridLayoutManager(this,2).apply {
            spanSizeLookup= object:GridLayoutManager.SpanSizeLookup(){
                override fun getSpanSize(position: Int): Int {
                    if(viewAdapter.isHeaderItem(position)||viewAdapter.isBottomItem(position)){
                        return 2;
                    }else{
                        return 1;
                    }

                }
            }
        }

        recyclerView.layoutManager=viewManager
        recyclerView.adapter = viewAdapter
        recyclerView.setOnHeaderUpdateListener(viewAdapter)

//        navController = findNavController(R.id.nav_host_fragment)
//        setDrawer()
//        // top level fragment destination setting
//        appBarConfiguration = AppBarConfiguration(
//                setOf(
//                        R.id.menu_my_info,
//                        R.id.menu_employee_list,
//                        R.id.menu_payment_details,
//                        R.id.menu_year_end_regulation,
//                        R.id.menu_user_management,
//                        R.id.menu_corporate_body_setting,
//                        R.id.menu_department_structure,
//                        R.id.menu_authority_setting,
//                        R.id.menu_master_setting
//                ), drawerMenu?.drawerLayout
//        )
//
//        // Set up ActionBar
//        setSupportActionBar(binding.toolbar)
//        // set back to top level control
//        setupActionBarWithNavController(navController, appBarConfiguration)
//
//        // setup the drawer with navigation controller
//        drawerMenu?.setupWithNavController(navController)
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }
//
//    override fun onUserProfileGet(img: String) {
//        UserPreferences.userThumbnail = img
//    }
//
//    override fun onBackPressed() {
//        //handle the back press :D close the drawer first and if the drawer is closed close the activity
//        if (drawerMenu.isDrawerOpen) {
//            drawerMenu.closeDrawer()
//        } else {
//            if(!navController.popBackStack()){
//                finish()
//            }
//            navController.navigateUp()
//        }
//    }

//    private fun setDrawer() {
//        drawerMenu = DrawerBuilder()
//                .withActivity(this)
//                .withToolbar(binding.toolbar)
//                .withTranslucentStatusBar(true)
//                .withHeader(R.layout.menu_nav_header)
//                .addDrawerItems(
//                        NavigationDrawerItem(
//                                R.id.menu_employee_list,
//                                PrimaryDrawerItem().withName(R.string.menu_employee_list).withIcon(R.drawable.ic_employees).withSelectable(
//                                        false
//                                )
//                        ),
//                        NavigationDrawerItem(
//                                R.id.menu_payment_details,
//                                PrimaryDrawerItem().withName(R.string.menu_payment_details).withIcon(R.drawable.ic_salary_detail).withSelectable(
//                                        false
//                                )
//                        ),
//                        NavigationDrawerItem(
//                                R.id.menu_year_end_regulation,
//                                PrimaryDrawerItem().withName(R.string.menu_year_end_regulation).withIcon(R.drawable.ic_salary_detail).withSelectable(
//                                        false
//                                )
//                        ),
//                        ExpandableDrawerItem().withName(R.string.menu_administrator_setting).withIcon(R.drawable.ic_setting).withSelectable(
//                                false
//                        ).withSubItems(
//                                NavigationDrawerItem(
//                                        R.id.menu_user_management,
//                                        SecondaryDrawerItem().withName(R.string.menu_user_setting).withLevel(2).withIcon(
//                                                GoogleMaterial.Icon.gmd_verified_user
//                                        ).withSelectable(false)
//                                ),
//                                NavigationDrawerItem(
//                                        R.id.menu_corporate_body_setting,
//                                        SecondaryDrawerItem().withName(R.string.menu_business_man_setting).withLevel(2).withIcon(
//                                                GoogleMaterial.Icon.gmd_copyright
//                                        ).withSelectable(false)
//                                ),
//                                NavigationDrawerItem(
//                                        R.id.menu_department_structure,
//                                        SecondaryDrawerItem().withName(R.string.menu_department_setting).withLevel(2).withIcon(
//                                                GoogleMaterial.Icon.gmd_settings_system_daydream
//                                        ).withSelectable(false)
//                                ),
//                                NavigationDrawerItem(
//                                        R.id.menu_authority_setting,
//                                        SecondaryDrawerItem().withName(R.string.menu_authority_setting).withLevel(2).withIcon(
//                                                GoogleMaterial.Icon.gmd_nature_people
//                                        ).withSelectable(false)
//                                ),
//                                NavigationDrawerItem(
//                                        R.id.menu_master_setting,
//                                        SecondaryDrawerItem().withName(R.string.menu_master_setting).withLevel(2).withIcon(
//                                                GoogleMaterial.Icon.gmd_perm_data_setting
//                                        ).withSelectable(false)
//                                )
//                        ),
//                        PrimaryDrawerItem().withName(R.string.menu_logout).withIcon(R.drawable.ic_logout).withSelectable(
//                                false
//                        )
//                                .withOnDrawerItemClickListener(
//                                        object : Drawer.OnDrawerItemClickListener {
//                                            override fun onItemClick(
//                                                    view: View?,
//                                                    position: Int,
//                                                    drawerItem: IDrawerItem<*>
//                                            ): Boolean {
//                                                logout()
//                                                return false
//                                            }
//                                        }
//                                ))
//                .build()

//        // set header layout click event
//        drawerMenu.header?.setOnClickListener {
//            drawerMenu.closeDrawer()
//            navController.navigate(R.id.action_global_headerFragment)
//        }
//        // bind data to header
//        drawerMenu.header?.let {
//            MenuNavHeaderBinding.bind(it).viewmodel = viewModel
//        }

    private fun logout() {
        AlertDialog.Builder(this)
                .setTitle(resources.getString(R.string.app_name))
                .setNegativeButton(resources.getString(R.string.app_name)) { _, _ ->
                }
                .setPositiveButton(resources.getString(R.string.app_name)) { _, _ ->
//                    val intent = Intent(this, LoginActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    startActivity(intent)
//                    finish()
//                    // sessionをクリアする
//                    UserPreferences.clearSesstion()
//                    UserPreferences.clearThumbinail()
                }.show()
    }
}
