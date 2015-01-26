import com.caseyscarborough.budget.security.Role
import com.caseyscarborough.budget.security.User
import com.caseyscarborough.budget.security.UserRole

class BootStrap {

    def init = { servletContext ->

        if (User.count == 0) {
            def me = new User(firstName: "Casey", lastName: "Scarborough", username: "casey", email: "caseyscarborough@gmail.com", password: "123")
            me.save(flush: true)

            def admin = new Role(authority: "ROLE_ADMIN")
            def user  = new Role(authority: "ROLE_USER")
            admin.save(flush: true)
            user.save(flush: true)

            UserRole.create(me, admin, true)
        }
    }
    def destroy = {
    }
}
