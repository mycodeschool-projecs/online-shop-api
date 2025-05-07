package mycode.online_shop_api.app.categories.mock;

import mycode.online_shop_api.app.cart.model.Cart;
import mycode.online_shop_api.app.categories.model.Category;
import mycode.online_shop_api.app.products.model.Product;
import mycode.online_shop_api.app.products.repository.ProductRepository;
import mycode.online_shop_api.app.users.model.User;

import java.util.HashSet;

import static mycode.online_shop_api.app.products.mocks.ProductMockData.createProduct;

public class CategoryMockData {

    public static Category createCategory(String name) {
        Category category = new Category();
        category.setId(1);
        category.setName(name);
        return category;
    }

    public static Category createElectronicsCategory() {


        return createCategory("Electronics");
    }
}
