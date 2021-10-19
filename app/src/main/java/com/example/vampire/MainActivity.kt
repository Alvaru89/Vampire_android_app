package com.example.vampire

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vampire.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.MobileAds
import android.util.DisplayMetrics
import android.support.v7.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val adSize: AdSize
        get() {
            val display = windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = ad_view_container.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
        }

//    adView.adUnitId = "ca-app-pub-3940256099942544/6300978111"

    lateinit var mAdView : AdView

    // TODO: Add adView to your view hierarchy.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this) {}
        val adView = AdView(this)
//        ad_view_container.addView(adView)
        mAdView = findViewById(R.id.adView)
        mAdView.adSize = adSize
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//        val agenda_txt : TextView = Agenda
//        val prestige_txt : TextView = Prestige
        val recyclerview: RecyclerView=findViewById(R.id.coterie)
        recyclerview.layoutManager=LinearLayoutManager(this,LinearLayout.VERTICAL,false )
        val coterie = mutableListOf<Vampire?>()
        var coterie_counter :Int =0

        var log: String  // pensar en el log

        //Base de datos de vampiros: nombre y blood
        val vampire_bbdd = listOf(listOf("Hydra",6),listOf("The Cossack",5),listOf("Brother",5),listOf("Beretta",5),listOf("Johnny",4),listOf("Shades",4),listOf("Skunk",3),listOf("Sweetums",3),listOf("Flick",3),listOf("Smoke",3),
        listOf("Guvnah",5),listOf("Doc",5),listOf("Bad Penny",4),listOf("Inmate #745943",4),listOf("Velvet",4),listOf("Karma",3),listOf("Lolita",3),listOf("Street preacher",2),listOf("Jesus",2),
        listOf("Lixue Chen",6),listOf("Bella Forte",5),listOf("Muhammad Zadeh",5),listOf("Bunny Benitez",4),listOf("Iris Lokken",4),listOf("Liza Holt",4),listOf("John Kartunen",3),listOf("Ty Smith",3),listOf("Bong-Cha Park",3),
        listOf("Randolph Marz",6),listOf("Stevie Osborn",5),listOf("Yusuf Kaya",5),listOf("Bruno Wagner",5),listOf("Zhang Wei",4),listOf("Montgomery White",4),listOf("Humberto Garcia",3),listOf("Nancy Witt",3),listOf("June Bryant",3))

        val add_prest: Button =findViewById(R.id.add_prest)
        val Prestige: TextView=findViewById(R.id.Prestige).toInt()
        add_prest.setOnClickListener{var number2 = Prestige+1
            log+="Prestige increased from $Prestige to $number2.\n"
            Prestige= number2}

        val sub_prest: Button =findViewById(R.id.sub_prest)
        val Prestige: TextView=findViewById(R.id.Prestige).toInt()
        sub_prest.setOnClickListener{var number2 = Prestige-1
            log+="Prestige decreased from $Prestige to $number2.\n"
            Prestige= number2}

        val add_agenda: Button =findViewById(R.id.add_agenda)
        val Agenda: TextView=findViewById(R.id.Agenda).toInt()
        add_agenda.setOnClickListener{var number2 = Agenda+1
            log+="Agenda increased from $Agenda to $number2.\n"
            Agenda= number2}

        val sub_agenda: Button =findViewById(R.id.sub_agenda)
        val Agenda: TextView=findViewById(R.id.Agenda).toInt()
        sub_agenda.setOnClickListener{var number2 = Agenda-1
            log+="Agenda increased from $Agenda to $number2.\n"
            Agenda= number2}



        val recruit: Button =findViewById(R.id.recruit)
        recruit.setOnClickListener{
            var vampire_name: String
            var blood_val:Int
            var leader:Boolean = false
            var influence: Int =0

            var selectbox =AlertDialog.Builder(this)
            selectbox.setTitle("Select vampire to recruit"){}
            selectbox.setItems(vampire_bbdd, {selectbox, which -> vampire_name=which
                for (i in 0..vampire_bbdd.size) {
                    if (vampire_name==vampire_bbdd.elementAt(i).elementAt(0)) {
                        blood_val = vampire_bbdd.elementAt(i).elementAt(1)}
                if (coterie_counter==0){leader=true;influence +=1;
                    var v0=Vampire(name=vampire_name,blood=blood_val, leader=leader,influence=influence)
                    coterie.add(v0)
                    coterie_counter=1}
                if (coterie_counter==1){
                    var v1=Vampire(name=vampire_name,blood=blood_val, leader=leader,influence=influence)
                    coterie.add(v1)
                    coterie_counter=2}
                Prestige-=blood_val
                log+="$vampire_name recruited (Prestige=$Prestige).\n"
                }
                )
            selectbox.setNegativeButton('Cancel'){ _, _ -> }
            selectbox.show()
            }
        }

        class Vampire(name: String, blood:Int,leader:Boolean =false,influence:Int =0,torpor:Boolean = false,dead:Boolean = false){
            init{
                var vamp_name_txt: TextView = findViewById(R.id.vampire_name)
                vamp_name_txt.text="$influence"

                var max_blood=blood
                var blood_val: TextView = findViewById(R.id.blood_val)
                blood_val.text="$blood / $max_blood"

                var leader_txt: TextView = findViewById(R.id.leader)
                if(leader=true){leader_txt.text = "LEADER" }
                else{leader_txt.text = ""}

                var torpor_dead_txt: TextView = findViewById(R.id.torpor_dead)
                torpor_dead_txt.text=""
                var influence_txt: TextView = findViewById(R.id.influence)
                influence_txt.text="Influence=$influence"
            }

            val mend: Button =findViewById(R.id.mend)
            mend.setOnClickListener{blood += 1
                if (blood==max_blood){torpor=false}}

            val damage: Button =findViewById(R.id.Damage)
            damage.setOnClickListener{
                blood -= 1
                if (blood==0){
                    var alertDialog = AlertDialog.Builder(this)
                    alertDialog.setTitle("Aggravated damage?")
                    alertDialog.setMessage("Any aggravated damage?")
                    alertDialog.setPositiveButton(android.R.string.yes) {
                        Prestige -= 1
                        blood=1
                        torpor=true
                        log+="$name goes to torpor (Prestige=$Prestige).\n"
                    }
                    alertDialog.setNegativeButton(android.R.string.no){
                        dead=true
                        log+="$name is dead.\n"
                    }
                    alertDialog.show()
                }

            val spend_inf: Button =findViewById(R.id.spend_inf )
            spend_inf.setOnClickListener{influence = 0}


            val add_inf: Button =findViewById(R.id.add_inf)
            add_inf.setOnClickListener{influence +=1}

            val alter_actions: List<String> = ListOf("Increase Blood potency","Decrease Blood Potency","Remove Vampire")
            val options: Button =findViewById(R.id.other)
                options.setOnClickListener{
                    var alert_other = AlertDialog.Builder(this)
                    alert_other.setTitle("Alternative actions")
                    alert_other.setItems(alter_actions,)
                    alert_other.setNegativeButton('Cancel'){ _, _ -> }
                }}
            fun remove_vampire(){
                // como meter en menu ...
                Prestige+=max_blood
                log+="$name removed (Prestige=$Prestige).\n"
                vampire=null
            }
            fun increase_maxblood(){
                // como meter en menu ...
                maxblood += 1
            }
            fun reduce_maxblood(){
                // como meter en menu ...
                maxblood -= 1
            }
        }//class vampire
    } //fun main
} //class main activity