package uz.pdp.kiyim_online_dokon.bot.session;

/**
 * Foydalanuvchi bot bilan muloqotdagi holatini belgilaydigan enum.
 * Har bir holat botning qaysi bosqichda ekanligini aniqlaydi.
 */
public enum UserState {

    MAIN_MENU,              // Asosiy menyu holati (bosh sahifa)

    SELECTING_CATEGORY,     // Foydalanuvchi kategoriyalardan birini tanlayapti

    SELECTING_PRODUCT,      // Foydalanuvchi mahsulotni tanlayapti (kategoriyadan yoki qidiruvdan)

    VIEWING_CART,           // Savatni ko'rish holati ("Savatim" bosilganda)

    SEARCHING,              // Foydalanuvchi qidiruv so'zini kiritmoqda

    SEARCHING_RESULTS,      // Qidiruv natijalarini ko'rish va mahsulot tanlash

    CONFIRMING_ORDER,       // Buyurtmani tasdiqlash ("Ha" yoki "Yo'q" kutilyapti)

    VIEWING_ORDERS,         // Foydalanuvchi o'z buyurtmalarini ko'rmoqda

    // YANGI HOLAT: Buyurtma tasdiqlangandan keyin lokatsiya kutilyapti
    WAITING_FOR_LOCATION    // Foydalanuvchidan geografik joylashuv (location) kutilmoqda
}