package com.example.fastdelivery.Activities.DataClasses

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.fastdelivery.Adapters.IntroSliderAdapter
import com.example.fastdelivery.Models.DataClasses.SliderItems
import com.example.fastdelivery.R
import com.example.fastdelivery.ViewModels.Splahs_view_model
import com.example.fastdelivery.ViewModels.shared_VM_ActNav_FragFoodDetails
import com.example.fastdelivery.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {
private lateinit var binding: ActivityMainBinding
private lateinit var l:ArrayList<SliderItems>
private lateinit var introslideradapter: IntroSliderAdapter
  private lateinit var model:Splahs_view_model
    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        model= ViewModelProvider(this)[Splahs_view_model::class.java]
        binding=ActivityMainBinding.inflate(layoutInflater)
        splashScreen.setKeepOnScreenCondition { true }
        setContentView(binding.root)

        model.isUserloging.observe(this){isloging->
      if (isloging){
          Log.d("observer","the observer get called")
           val intent=Intent(this,Navigation_Activity::class.java)
           startActivity(intent)
          finish()
      } else  splashScreen.setKeepOnScreenCondition { false }
        }

        l=ArrayList()
        l.add(SliderItems("title1","description1 test test", R.drawable.introimage2))
        l.add(SliderItems("title2","description2" ,R.drawable._4688799_8209955__1_))
        l.add(SliderItems("title3","description3", R.drawable._4688799_8209955__1_))
        l.add(SliderItems("title4","description4", R.drawable._4688799_8209955__1_))
        introslideradapter= IntroSliderAdapter(l)
        binding.introviewpager2.adapter=introslideradapter

        //set the indicator
        binding.indicator.setViewPager(binding.introviewpager2)

        //the next button
        binding.Nextbtn.setOnClickListener {
         if (binding.introviewpager2.currentItem==3) {
             val intent=Intent(this,LoginActivity::class.java)
             startActivity(intent)
         }else binding.introviewpager2.setCurrentItem( binding.introviewpager2.currentItem +1, true)
        }
        // the skip button
        binding.Skip.setOnClickListener {
            binding.introviewpager2.setCurrentItem( 3, true)
        }
       // when it's last page
        binding.introviewpager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int){
                super.onPageSelected(position)
                if (position == 3) {
                    Log.d("this","this is called")
                    binding.Skip.text = ""
                    binding.Nextbtn.text = "Get Started"

                } else {
                    binding.Skip.text = "Skip"
                    binding.Nextbtn.text = "Next"

                }
            }
        })
        // remove the overscroll animation
        binding.introviewpager2.getChildAt(0).overScrollMode= RecyclerView.OVER_SCROLL_NEVER


    }
}
