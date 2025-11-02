/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sebas.lab.testing;

/**
 *
 * @author jacks
 */
public class UserService {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public String getUserName(String id) {
        User user = repository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user.getName();
    }
}
