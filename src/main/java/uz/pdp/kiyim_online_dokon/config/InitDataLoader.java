// src/main/java/uz/pdp/kiyim_online_dokon/config/InitDataLoader.java

package uz.pdp.kiyim_online_dokon.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.kiyim_online_dokon.entity.Category;
import uz.pdp.kiyim_online_dokon.entity.Products;
import uz.pdp.kiyim_online_dokon.entity.Role;
import uz.pdp.kiyim_online_dokon.repository.CategoryRepository;
import uz.pdp.kiyim_online_dokon.repository.ProductsRepository;
import uz.pdp.kiyim_online_dokon.repository.RoleRepository;
import uz.pdp.kiyim_online_dokon.repository.UsersRepository;

@Component
@RequiredArgsConstructor
public class InitDataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final ProductsRepository productRepository;

    @Override
    public void run(String... args) {
        System.out.println("\n🚀 InitDataLoader ishga tushdi...\n");

        // Rollarni yaratish
        Role roleAdmin = createRoleIfNotExists("ROLE_ADMIN");
        Role roleUser = createRoleIfNotExists("ROLE_USER");

        // Kategoriyalarni yaratish (faqat name bilan)
        Category erkakKiyim = createCategoryIfNotExists("Erkaklar kiyimi");
        Category ayolKiyim = createCategoryIfNotExists("Ayollar kiyimi");
        Category bolalarKiyim = createCategoryIfNotExists("Bolalar kiyimi");
        Category poyabzal = createCategoryIfNotExists("Poyabzal");
        Category aksessuarlar = createCategoryIfNotExists("Aksessuarlar");

        // Mahsulotlarni yaratish
        createProductIfNotExists("Erkaklar ko'ylagi", "Klassik oq rangdagi erkaklar ko'ylagi",
                250000.0, 50, erkakKiyim);
        createProductIfNotExists("Erkaklar shimlar", "Biznes uslubidagi qora shimlar",
                350000.0, 30, erkakKiyim);
        createProductIfNotExists("Ayollar ko'ylagi", "Zamonaviy dizayndagi ayollar ko'ylagi",
                180000.0, 45, ayolKiyim);
        createProductIfNotExists("Ayollar yubkasi", "Klassik uslubdagi yubka",
                220000.0, 35, ayolKiyim);
        createProductIfNotExists("Bolalar futbolkasi", "Rangli va qulay futbolka",
                80000.0, 100, bolalarKiyim);
        createProductIfNotExists("Erkaklar tuflisi", "Charm erkaklar tuflisi",
                450000.0, 25, poyabzal);
        createProductIfNotExists("Ayollar tufli", "Zamonaviy dizayndagi ayollar tuflisi",
                380000.0, 30, poyabzal);
        createProductIfNotExists("Qo'l soati", "Klassik uslubdagi qo'l soati",
                650000.0, 15, aksessuarlar);
        createProductIfNotExists("Charm kamar", "Sifatli charm kamar",
                150000.0, 40, aksessuarlar);

        System.out.println("\n✅ InitDataLoader muvaffaqiyatli yakunlandi!\n");
    }

    private Role createRoleIfNotExists(String roleName) {
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            role = new Role();
            role.setName(roleName);
            role = roleRepository.save(role);
            System.out.println("  ➕ Role: " + roleName);
        }
        return role;
    }

    private Category createCategoryIfNotExists(String name) {
        Category category = categoryRepository.findByName(name);
        if (category == null) {
            category = new Category();
            category.setName(name);
            category = categoryRepository.save(category);
            System.out.println("  ➕ Category: " + name);
        }
        return category;
    }

    private Products createProductIfNotExists(String name, String description,
                                              double price, Integer stock, Category category) {
        Products product = productRepository.findByName(name);
        if (product == null) {
            product = new Products();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setStock(stock);
            product.setCategory(category);
            product = productRepository.save(product);
            System.out.println("  ➕ Product: " + name + " (" + category.getName() + ")");
        }
        return product;
    }
}