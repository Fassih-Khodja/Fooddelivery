package com.example.fastdelivery.Activities.DataClasses

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.fastdelivery.R
import com.example.fastdelivery.ViewModels.Navigation_User_VM
import com.example.fastdelivery.ViewModels.shared_VM_ActNav_FragFoodDetails
import com.example.fastdelivery.databinding.ActivityNavigationBinding

// remarque , i'm using nvigation component to navigate ...

class Navigation_Activity : AppCompatActivity() {
    lateinit var binding: ActivityNavigationBinding
    private lateinit var navController: NavController

    // setting up the viewmodel
     val model: shared_VM_ActNav_FragFoodDetails by viewModels()
    val user_model:Navigation_User_VM by viewModels()
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    /*  val  resultLauncher = registerForActivityResult(
            // here i will register the launcher with a contract. A contract defines what type of result you're expecting
            // in my case the type is a startactivity for result
            ActivityResultContracts.StartActivityForResult()
        ) { result -> // this is a callback executed every time the activity do (set result) , and will not executed on the first time
          Log.d("the result","the result get called")
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("the result.code","good")
                //here i will Handle the result here
                val data = result.data
                //result.data retrieves the Intent that was sent back from the second activity. This Intent contains any data that was passed using setResult() from the second activity.
                val updatedData = data?.getParcelableArrayListExtra<Cart>("CartList2")
                model.cartlist.value=updatedData
            } else  Log.d("the result.code","bad")
        }*/
        binding=ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)





        // this is the bottom navigation bar
      //  val navView: BottomNavigationView = binding.bottomNavigation
      //  NavHostFragment: It is the container fragment that holds your navigation graph
        // this is access to the FragmentManager by supportFragmentManager
      //  supportFragmentManager.commit { addToBackStack("test") }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment

        // the fragment container is our navhostfragment
        navController = navHostFragment.navController




        val headerView= binding.navView.getHeaderView(0) // Get header view
        val headername: TextView = headerView.findViewById(R.id.user_name_text_nav_draw)
 val headeremail: TextView = headerView.findViewById(R.id.user_email_text_nav_draw)



        user_model.userInfo.observe(this){ user ->
            if (user != null) {
                headername.text = user.full_name
                headeremail.text = user.email
            }
        }


        // Set up Bottom Navigation Bar with NavController
        // there is this on the documentation
       // navView.setupWithNavController(navController)
       /* navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.foodDetailsChild -> {
                    navView.menu.findItem(R.id.home).isChecked = true
                }

                // Add more cases for other bottom nav fragments if necessary
            }*/


/*binding.btncart.setOnClickListener {
    Log.d("the acttivity","btn test")
    val intent = Intent(this, My_Cart::class.java)
    val cartList = model.cartlist.value ?: arrayListOf()
    intent.putParcelableArrayListExtra("CartList",ArrayList(cartList) )
    resultLauncher.launch(intent)
}*/




            binding.btnmenu.setOnClickListener {
                binding.myDrawerLayout.openDrawer(GravityCompat.START)
            }


            binding.navView.setNavigationItemSelectedListener { menuItem ->

                when (menuItem.itemId) {
                    R.id.main_container -> {
                        Toast.makeText(this, "Home Clicked", Toast.LENGTH_SHORT).show()
                        if (navController.currentDestination?.id != R.id.main_container) {
                            navController.popBackStack(R.id.main_container, false) // Pop if exists
                            navController.navigate(R.id.main_container) // Navigate only if not found
                        }
                    }
                    R.id.profile -> {
                        Toast.makeText(this, "Profile Clicked", Toast.LENGTH_SHORT).show()
                        if (navController.currentDestination?.id != R.id.profile) {
                            navController.popBackStack(R.id.profile, false) // Pop if exists
                            navController.navigate(R.id.profile) // Navigate only if not found
                        }
                    }
                    R.id.notification -> {
                        Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show()
                    }
else->false
                }
                binding.myDrawerLayout.closeDrawer(GravityCompat.START) // Close drawer after selection
                true
            }



      navController.addOnDestinationChangedListener { _, destination, _ -> // handle the checked item
          val menu = binding.navView.menu
          for (i in 0 until menu.size()) {
              val menuItem1 = menu.getItem(i)
              menuItem1.isChecked = (menuItem1.itemId == destination.id)
          }
      }




    }






      /*  fun isFragmentInBackstack(navController: NavController, destinationId: Int): Boolean {
            return try {
                navController.getBackStackEntry(destinationId)
                Log.d("it's true","true")

                true // If no exception, the fragment is in the backstack
            } catch (e: IllegalArgumentException) {
                false // If exception is thrown, the fragment is not in the backstack
            }
        }*/


    /*   navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    if(isFragmentInBackstack(navController, R.id.foodDetailsChild)){
                        navController.popBackStack(R.id.foodDetailsChild,false)
                    }
                    else
                        navController.popBackStack(R.id.home,false)
                    true
                }
                R.id.search->{
                    navController.navigate(R.id.search)
                    true
                }
                R.id.favorite -> {
                    if(isFragmentInBackstack(navController, R.id.favorite)){
                        Log.d("isthere","true isthere")
                        if ( navController.currentBackStackEntry!!.destination.id == R.id.orders){ // the currentBackStackEntry is the on on the top of the backstack
                            Log.d("currentbackstack","true previous")
                            // this will always true , cuz it's impossible for home to be the current and at the same time isFragmentInBackstack
                            navController.popBackStack(R.id.favorite,true)
                            navController.navigate(R.id.orders)
                            navController.navigate(R.id.favorite)
                        }
                        else  navController.popBackStack(R.id.favorite,false) // there is no need for this line
                    }
                    else navController.navigate(R.id.favorite)
                    true
                }
                R.id.orders -> {
                    if(isFragmentInBackstack(navController, R.id.orders)){
                        Log.d("isthere","true isthere")
                        if ( navController.currentBackStackEntry!!.destination.id == R.id.favorite){ // the currentBackStackEntry is the on on the top of the backstack
                            Log.d("currentbackstack","true previous")
                            // this will always true , cuz it's impossible for home to be the current and at the same time isFragmentInBackstack
                            navController.popBackStack(R.id.orders,true)
                            navController.navigate(R.id.favorite)
                            navController.navigate(R.id.orders)
                        }
                        else  navController.popBackStack(R.id.orders,false) // there is no need for this line
                    }
                    else navController.navigate(R.id.orders)
                    true
                }
                else -> false
            }
        }

        navView.setOnItemReselectedListener {  item -> /// this when reselected and you are on it
            when (item.itemId) {
                R.id.home -> {
                    //Here the NavController pops back to the destination with the integer id destinationId
                    // as the inclusive argument is false , he will not pops off the home fragment from back stack , tht means he will not remove it from the backstack
                    navController.popBackStack(R.id.home, false) // for home iwill always do that
                    /*It will not remove HomeFragment from the backstack.
                     Instead, it will pop fragments that are on top of HomeFragment
                     until HomeFragment is the visible fragment*/
                    true
                }

        }
    }


}*/

    override fun onResume() {
        super.onResume()
    }
    override fun onBackPressed() {
        // Close drawer if open, otherwise exit app
        if (binding.myDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.myDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


}


// explication about how he know which distination
/*Tie destinations to menu items
NavigationUI also provides helpers for tying destinations to menu-driven UI components.
 NavigationUI contains a helper method, onNavDestinationSelected(),
  which takes a MenuItem along with the NavController that hosts the associated destination.
   If the id of the MenuItem matches the id of the destination,
   the NavController can then navigate to that destination.*/

// remarque ,,pop off backstack means removing the fragment from the backstack
