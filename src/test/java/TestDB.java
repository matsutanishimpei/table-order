import database.impl.UserDAOImpl;
import database.impl.ProductDAOImpl;
import database.impl.CategoryDAOImpl;
import database.impl.OrderDAOImpl;
import model.User;
import model.Product;
import model.Category;
import java.util.List;

public class TestDB {
    public static void main(String[] args) {
        try {
            System.out.println("--- Test UserDAO ---");
            UserDAOImpl uDao = new UserDAOImpl();
            List<User> users = uDao.findAll();
            System.out.println("Users count: " + users.size());

            System.out.println("--- Test ProductDAO ---");
            ProductDAOImpl pDao = new ProductDAOImpl();
            List<Product> prods = pDao.findAll();
            System.out.println("Products count: " + prods.size());

            System.out.println("--- Test CategoryDAO ---");
            CategoryDAOImpl cDao = new CategoryDAOImpl();
            List<Category> cats = cDao.findAll();
            System.out.println("Categories count: " + cats.size());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
