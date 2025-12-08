package uz.pdp.kiyim_online_dokon.bot.session;

public enum UserState {
    // Asosiy menyu holati
    MAIN_MENU,
    // Mahsulot qidirish holati (foydalanuvchi qidiruv so'zini yozadi)
    SEARCHING,
    // Qidiruv natijalari ko'rsatilgan holat
    SEARCHING_RESULTS,
    // Kategoriya ichidagi mahsulotlarni tanlash holati
    SELECTING_PRODUCT,
    // Savatni ko'rsatish/tekshirish holati
    VIEWING_CART,
    // Buyurtma berish uchun manzil kiritish holati
    ENTERING_ADDRESS
}