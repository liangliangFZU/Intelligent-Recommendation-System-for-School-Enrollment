package com.example.login.dao;
import com.example.login.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
public interface UserRepository extends JpaRepository<User,Long> {
    public List<User> findByUsername(String username);
    public List<User> findByMail(String mail);
    public List<User> findByMailAndPassword(String mail,String password);
}
