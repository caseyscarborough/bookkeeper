import com.caseyscarborough.budget.Account
import com.caseyscarborough.budget.AccountType
import com.caseyscarborough.budget.Category
import com.caseyscarborough.budget.CategoryType
import com.caseyscarborough.budget.SubCategory
import com.caseyscarborough.budget.Transaction
import com.caseyscarborough.budget.security.Role
import com.caseyscarborough.budget.security.User
import com.caseyscarborough.budget.security.UserRole
import grails.converters.JSON

class BootStrap {

  def init = { servletContext ->

    JSON.registerObjectMarshaller(Transaction) { Transaction t ->
      return [
          id: t.id,
          description: t.description,
          date: t.date.format("M/d/yyyy"),
          amount: t.amount,
          fromAccount: [
              id: t.fromAccount.description,
              description: t.fromAccount.description
          ],
          toAccount: [
              id: t.toAccount?.description,
              description: t.toAccount?.description
          ],
          subCategory: [
              id: t.subCategory.id,
              name: t.subCategory.name
          ],
          cssClass: t.cssClass
      ]
    }

    JSON.registerObjectMarshaller(AccountType) { AccountType a ->
      return [
          value: a.name(),
          name: a.name,
          isDebt: a.isDebt
      ]
    }

    JSON.registerObjectMarshaller(CategoryType) { CategoryType c ->
      return [
          value: c.name(),
          name: c.name
      ]
    }

    JSON.registerObjectMarshaller(Account) { Account a ->
      return [
          id: a.id,
          description: a.description,
          balance: a.balance,
          type: a.type
      ]
    }

    JSON.registerObjectMarshaller(SubCategory) { SubCategory s ->
      return [
          id: s.id,
          name: s.name,
          type: s.type
      ]
    }

    JSON.registerObjectMarshaller(Category) { Category c ->
      return [
          id: c.id,
          name: c.name,
          subcategories: c.subcategories,
          type: c.type
      ]
    }

    if (User.count == 0) {
      log.debug("Creating default user...")
      def me = new User(firstName: "Casey", lastName: "Scarborough", username: "casey", email: "caseyscarborough@gmail.com", password: "123")
      me.save(flush: true)

      def admin = new Role(authority: "ROLE_ADMIN")
      def user = new Role(authority: "ROLE_USER")
      admin.save(flush: true)
      user.save(flush: true)

      UserRole.create(me, admin, true)
    }

    if (Category.count == 0) {
      log.debug("Creating categories...")
      def income = new Category(name: "Income", type: CategoryType.CREDIT)
      income.addToSubcategories(new SubCategory(name: "Wages"))
      income.addToSubcategories(new SubCategory(name: "Gift"))
      income.addToSubcategories(new SubCategory(name: "Interest"))
      income.addToSubcategories(new SubCategory(name: "Refund"))
      income.addToSubcategories(new SubCategory(name: "Transfer"))
      income.addToSubcategories(new SubCategory(name: "Other"))
      income.save(flush: true)

      def home = new Category(name: "Home/Bills", type: CategoryType.DEBIT)
      home.addToSubcategories(new SubCategory(name: "Electricity"))
      home.addToSubcategories(new SubCategory(name: "Gas"))
      home.addToSubcategories(new SubCategory(name: "Credit Card Payment", type: CategoryType.TRANSFER))
      home.addToSubcategories(new SubCategory(name: "Home/Rental Insurance"))
      home.addToSubcategories(new SubCategory(name: "Furnishings"))
      home.addToSubcategories(new SubCategory(name: "Appliances"))
      home.addToSubcategories(new SubCategory(name: "Maintenance"))
      home.addToSubcategories(new SubCategory(name: "Lawn/Garden"))
      home.addToSubcategories(new SubCategory(name: "Cable"))
      home.addToSubcategories(new SubCategory(name: "Internet"))
      home.addToSubcategories(new SubCategory(name: "Trash"))
      home.addToSubcategories(new SubCategory(name: "Water"))
      home.addToSubcategories(new SubCategory(name: "Rent"))
      home.addToSubcategories(new SubCategory(name: "Other"))
      home.save(flush: true)

      def pets = new Category(name: "Pets", type: CategoryType.DEBIT)
      pets.addToSubcategories(new SubCategory(name: "Veterinarian"))
      pets.addToSubcategories(new SubCategory(name: "Pet Supplies"))
      pets.addToSubcategories(new SubCategory(name: "Pet Toys"))
      pets.addToSubcategories(new SubCategory(name: "Pet Food"))
      pets.addToSubcategories(new SubCategory(name: "Other"))
      pets.save(flush: true)

      def food = new Category(name: "Food", type: CategoryType.DEBIT)
      food.addToSubcategories(new SubCategory(name: "Groceries"))
      food.addToSubcategories(new SubCategory(name: "Restaurants"))
      food.addToSubcategories(new SubCategory(name: "Fast Food"))
      food.addToSubcategories(new SubCategory(name: "Alcohol"))
      food.addToSubcategories(new SubCategory(name: "Other"))
      food.save(flush: true)

      def health = new Category(name: "Health", type: CategoryType.DEBIT)
      health.addToSubcategories(new SubCategory(name: "Health Insurance"))
      health.addToSubcategories(new SubCategory(name: "Doctor"))
      health.addToSubcategories(new SubCategory(name: "Dentist"))
      health.addToSubcategories(new SubCategory(name: "Medicine/Drugs"))
      health.addToSubcategories(new SubCategory(name: "Life Insurance"))
      health.addToSubcategories(new SubCategory(name: "Gym Membership"))
      health.addToSubcategories(new SubCategory(name: "Other"))
      health.save(flush: true)

      def entertainment = new Category(name: "Entertainment", type: CategoryType.DEBIT)
      entertainment.addToSubcategories(new SubCategory(name: "Videos/DVD"))
      entertainment.addToSubcategories(new SubCategory(name: "Netflix"))
      entertainment.addToSubcategories(new SubCategory(name: "Spotify"))
      entertainment.addToSubcategories(new SubCategory(name: "Music"))
      entertainment.addToSubcategories(new SubCategory(name: "Movies"))
      entertainment.addToSubcategories(new SubCategory(name: "Video Games"))
      entertainment.addToSubcategories(new SubCategory(name: "Books"))
      entertainment.addToSubcategories(new SubCategory(name: "Travel"))
      entertainment.addToSubcategories(new SubCategory(name: "Gadgets"))
      entertainment.addToSubcategories(new SubCategory(name: "Technology"))
      entertainment.addToSubcategories(new SubCategory(name: "Concerts"))
      entertainment.addToSubcategories(new SubCategory(name: "Other"))
      entertainment.save(flush: true)

      def personal = new Category(name: "Personal", type: CategoryType.DEBIT)
      personal.addToSubcategories(new SubCategory(name: "Hygiene Supplies"))
      personal.addToSubcategories(new SubCategory(name: "Clothing"))
      personal.addToSubcategories(new SubCategory(name: "Barber/Salon"))
      personal.addToSubcategories(new SubCategory(name: "Other"))
      personal.save(flush: true)

      def transportation = new Category(name: "Transportation", type: CategoryType.DEBIT)
      transportation.addToSubcategories(new SubCategory(name: "Fuel"))
      transportation.addToSubcategories(new SubCategory(name: "Auto Insurance"))
      transportation.addToSubcategories(new SubCategory(name: "Vehicle Payment"))
      transportation.addToSubcategories(new SubCategory(name: "Bus/Taxi/Train Fare"))
      transportation.addToSubcategories(new SubCategory(name: "Repairs"))
      transportation.addToSubcategories(new SubCategory(name: "License/Registration"))
      transportation.addToSubcategories(new SubCategory(name: "Other"))
      transportation.save(flush: true)

      def savings = new Category(name: "Savings", type: CategoryType.DEBIT)
      savings.addToSubcategories(new SubCategory(name: "Transfer", type: CategoryType.TRANSFER))
      savings.addToSubcategories(new SubCategory(name: "Emergency Fund"))
      savings.addToSubcategories(new SubCategory(name: "Retirement"))
      savings.addToSubcategories(new SubCategory(name: "Other"))
      savings.save(flush: true)

      def misc = new Category(name: "Miscellaneous", type: CategoryType.DEBIT)

    }
  }
  def destroy = {
  }
}
