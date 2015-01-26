import com.caseyscarborough.budget.Category
import com.caseyscarborough.budget.CategoryType
import com.caseyscarborough.budget.SubCategory
import com.caseyscarborough.budget.security.Role
import com.caseyscarborough.budget.security.User
import com.caseyscarborough.budget.security.UserRole

class BootStrap {

  def init = { servletContext ->

    if (User.count == 0) {
      def me = new User(firstName: "Casey", lastName: "Scarborough", username: "casey", email: "caseyscarborough@gmail.com", password: "123")
      me.save(flush: true)

      def admin = new Role(authority: "ROLE_ADMIN")
      def user = new Role(authority: "ROLE_USER")
      admin.save(flush: true)
      user.save(flush: true)

      UserRole.create(me, admin, true)
    }

    if (Category.count == 0) {
      def income = new Category(name: "Income", type: CategoryType.CREDIT)
      income.addToSubcategories(new SubCategory(name: "Wages"))
      income.addToSubcategories(new SubCategory(name: "Gift"))
      income.save(flush: true)

      def home = new Category(name: "Home", type: CategoryType.DEBIT)
      home.addToSubcategories(new SubCategory(name: "Electric"))
      home.addToSubcategories(new SubCategory(name: "Gas"))
      home.addToSubcategories(new SubCategory(name: "Cable"))
      home.addToSubcategories(new SubCategory(name: "Internet"))
      home.addToSubcategories(new SubCategory(name: "Trash"))
      home.addToSubcategories(new SubCategory(name: "Water"))
      home.save(flush: true)
    }
  }
  def destroy = {
  }
}
