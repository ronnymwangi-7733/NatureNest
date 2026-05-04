package com.ronny.naturenest.models

object NatureNestRepository {

    fun getOnboardingPages(): List<OnboardingPage> = listOf(
        OnboardingPage(
            title = "Welcome to\nNatureNest 🌿",
            subtitle = "Your pregnancy & motherhood companion",
            description = "From the first heartbeat to your baby's first steps — we're with you every step of the journey.",
            emoji = "🤰",
            backgroundColor = 0xFFFDE8EE
        ),
        OnboardingPage(
            title = "Track Every\nPrecious Moment",
            subtitle = "Week-by-week pregnancy insights",
            description = "Follow your baby's development, monitor your health, and get personalized tips tailored to your stage.",
            emoji = "📅",
            backgroundColor = 0xFFE4F2EA
        ),
        OnboardingPage(
            title = "Never Miss\nImportant Care",
            subtitle = "Smart reminders & health tracking",
            description = "Clinic visits, medications, nutrition goals — we help you stay on top of your wellbeing and your baby's development.",
            emoji = "💊",
            backgroundColor = 0xFFFFF0E4
        ),
        OnboardingPage(
            title = "A Community\nJust for Moms",
            subtitle = "Connect, share & grow together",
            description = "Access expert advice, helpful articles, and thousands of moms who truly understand your journey.",
            emoji = "💕",
            backgroundColor = 0xFFF2EBF9
        )
    )

    fun getPregnancyWeekInfo(week: Int): PregnancyWeekInfo {
        return when (week) {
            1, 2, 3, 4 -> PregnancyWeekInfo(
                week = week,
                babySize = "Poppy Seed",
                babyWeight = "< 1g",
                babyLength = "0.1 cm",
                babyDevelopment = "Fertilization has occurred and the embryo is implanting in your uterus. The neural tube, which will become the brain and spinal cord, is beginning to form.",
                momChanges = "You may notice light spotting from implantation. Hormones are surging, possibly causing tender breasts and fatigue.",
                tips = listOf(
                    "Start taking prenatal vitamins with folic acid",
                    "Avoid alcohol and smoking",
                    "Schedule your first prenatal appointment",
                    "Stay hydrated with at least 8 glasses of water"
                ),
                emoji = "🌱"
            )
            5, 6, 7, 8 -> PregnancyWeekInfo(
                week = week,
                babySize = "Blueberry",
                babyWeight = "1g",
                babyLength = "1.6 cm",
                babyDevelopment = "Your baby's heart is beating about 150 times per minute. Tiny arm and leg buds are forming. The brain, spinal cord, and other organs are beginning to form.",
                momChanges = "Morning sickness may peak during these weeks. Your uterus is growing, and you may feel bloated and need to urinate more frequently.",
                tips = listOf(
                    "Eat small, frequent meals to combat nausea",
                    "Get plenty of rest",
                    "Avoid strong smells that trigger nausea",
                    "Try ginger tea for morning sickness relief"
                ),
                emoji = "🫐"
            )
            9, 10, 11, 12 -> PregnancyWeekInfo(
                week = week,
                babySize = "Lime",
                babyWeight = "14g",
                babyLength = "5.4 cm",
                babyDevelopment = "All vital organs are formed! Baby's fingers and toes are distinct, and tiny fingernails are forming. Baby can make small movements.",
                momChanges = "Morning sickness may start easing. You may notice your waistline thickening. Energy levels may begin to return.",
                tips = listOf(
                    "Consider sharing your pregnancy news",
                    "Schedule first trimester screening",
                    "Begin gentle prenatal exercises",
                    "Focus on iron-rich foods"
                ),
                emoji = "🍋"
            )
            else -> PregnancyWeekInfo(
                week = week,
                babySize = "Mango",
                babyWeight = "300g",
                babyLength = "25 cm",
                babyDevelopment = "Your baby is growing rapidly! They can hear sounds and may respond to your voice. Movement is becoming more regular.",
                momChanges = "You may feel your baby move — called quickening. Your bump is clearly visible now. Back aches may begin.",
                tips = listOf(
                    "Talk and sing to your baby",
                    "Practice pelvic floor exercises",
                    "Consider pregnancy yoga or swimming",
                    "Sleep on your left side for better circulation"
                ),
                emoji = "🥭"
            )
        }
    }

    fun getHealthTips(): List<HealthTip> = listOf(
        HealthTip(1, "Iron-Rich Foods During Pregnancy", "Iron is crucial for making hemoglobin — the protein in red blood cells that carries oxygen to your tissues and to your baby. Learn which foods are best.", TipCategory.NUTRITION, "4 min read"),
        HealthTip(2, "Gentle Prenatal Yoga Poses", "Safe yoga poses to ease back pain, improve sleep, and prepare your body for labor. Always consult your doctor first.", TipCategory.EXERCISE, "6 min read"),
        HealthTip(3, "Managing Pregnancy Anxiety", "It's completely normal to feel anxious during pregnancy. Discover evidence-based techniques to calm your mind and nurture your emotional wellbeing.", TipCategory.MENTAL_HEALTH, "5 min read"),
        HealthTip(4, "Swaddling Your Newborn", "Learn the proper technique to swaddle your newborn safely for better sleep and comfort. Step-by-step photo guide included.", TipCategory.BABY_CARE, "3 min read"),
        HealthTip(5, "Postpartum Recovery Timeline", "What to expect in the first 6 weeks after birth — physical changes, emotional shifts, and when to call your doctor.", TipCategory.POSTPARTUM, "8 min read"),
        HealthTip(6, "Folic Acid: Why It Matters", "Folic acid helps prevent neural tube defects. Find out how much you need and the best food sources to include in your diet.", TipCategory.PRENATAL, "3 min read"),
        HealthTip(7, "Breastfeeding Tips for New Moms", "Getting started with breastfeeding can be challenging. Here are expert-backed tips to help you and your baby find a comfortable routine.", TipCategory.BABY_CARE, "7 min read"),
        HealthTip(8, "Hydration During Pregnancy", "Staying well-hydrated is more important than ever during pregnancy. Learn how much water you need and creative ways to stay hydrated.", TipCategory.NUTRITION, "3 min read")
    )

    fun getReminders(): List<Reminder> = listOf(
        Reminder(1, "OB-GYN Appointment", "20-week anomaly scan — Dr. Wanjiku, Nairobi Women's Hospital", "10:00 AM", "Tomorrow", ReminderType.CLINIC_VISIT),
        Reminder(2, "Prenatal Vitamin", "Take folic acid + iron supplement with breakfast", "8:00 AM", "Daily", ReminderType.MEDICATION),
        Reminder(3, "Prenatal Vitamin Evening", "Take vitamin D + calcium supplement", "8:00 PM", "Daily", ReminderType.SUPPLEMENT),
        Reminder(4, "Drink Water Goal", "Aim for 8 glasses of water today", "All Day", "Daily", ReminderType.NUTRITION),
        Reminder(5, "Prenatal Yoga Session", "30-minute gentle yoga — YouTube Prenatal Yoga", "6:30 AM", "Mon, Wed, Fri", ReminderType.EXERCISE),
        Reminder(6, "Blood Pressure Check", "Home BP monitoring — note readings in journal", "9:00 AM", "Weekly", ReminderType.CLINIC_VISIT)
    )

    fun getCommunityPosts(): List<CommunityPost> = listOf(
        CommunityPost(1, "Amina K.", "AK", "Week 28", "Just felt my baby kick for the first time and I literally cried happy tears! 😭💕 Is it normal that it feels like little bubbles at first? Second-time moms, tell me everything!", 47, 23, "2h ago", "Pregnancy Milestones"),
        CommunityPost(2, "Grace M.", "GM", "Postpartum (8 weeks)", "Postpartum hair loss is SO REAL. Nobody warned me about this! 😅 Lost a huge chunk in the shower today. Anyone else? When does it stop?", 89, 41, "4h ago", "Postpartum Journey"),
        CommunityPost(3, "Njeri W.", "NW", "Week 12", "Just had my first trimester scan and baby is perfect! 🙌 Doctor said everything looks great. After 2 miscarriages, this moment means everything. Sending love to all the warrior mamas here ❤️", 203, 67, "6h ago", "Good News"),
        CommunityPost(4, "Fatuma A.", "FA", "Mom of 2", "Recipe share: These pregnancy-safe energy balls saved my life in the first trimester when I couldn't eat anything! Oats + banana + peanut butter + honey. So easy and actually stayed down! 🙏", 134, 28, "Yesterday", "Nutrition Tips"),
        CommunityPost(5, "Wambui C.", "WC", "Week 35", "Hospital bag packed at 35 weeks! ✅ Sharing my list in case it helps anyone: baby clothes (3 outfits), receiving blankets, your own pillow, snacks for labor, phone charger, and comfy going-home outfit. What did I miss?", 167, 89, "Yesterday", "Birth Prep")
    )

    fun getNutritionData(): List<NutritionItem> = listOf(
        NutritionItem("Water", "6", "glasses", 6f, 8f, 0xFF4FC3F7),
        NutritionItem("Folic Acid", "400", "mcg", 400f, 600f, 0xFF81C784),
        NutritionItem("Iron", "21", "mg", 21f, 27f, 0xFFFFB74D),
        NutritionItem("Calcium", "800", "mg", 800f, 1000f, 0xFFBA68C8),
        NutritionItem("Protein", "68", "g", 68f, 75f, 0xFFFF8A65)
    )
}