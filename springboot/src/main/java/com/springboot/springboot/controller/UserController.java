package com.springboot.springboot.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.springboot.entity.AdminUsers;
import com.springboot.springboot.entity.Books;
import com.springboot.springboot.entity.MemberShip;
import com.springboot.springboot.repository.AdminUsersRepo;
import com.springboot.springboot.repository.BooksRepo;
import com.springboot.springboot.repository.MemberShipRepo;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private AdminUsersRepo adminUsersRepo;

    @Autowired
    private BooksRepo booksRepo;

    @Autowired
    private MemberShipRepo memberShipRepo;

    @PostMapping("/adminLogin")
    public Boolean adminLogin(@RequestBody AdminUsers adminUsers)
    {
        String email = adminUsers.getEmail();
        String password = adminUsers.getPassword();
        List<AdminUsers>li = adminUsersRepo.adminLogin(email, password);
        if(li.size()>0)
        {
            return true;
        }
        return false;
    }

    @PostMapping("/addBook")
    public void addBooks(@RequestBody Books books)
    {
        booksRepo.save(books);    
    }

    @GetMapping("/getAllBooks")
    public List<Books> getAllBooks() {
        return booksRepo.findAll();
    }

    @PostMapping("/newMember")
    public String newMember(@RequestBody MemberShip memberShip) {
        MemberShip memberShip2 = memberShipRepo.findById(memberShip.getMobileNumber()).orElse(null);
        if(memberShip2 == null) 
        {
            memberShipRepo.save(memberShip);
            return "Request Submitted";
        }
        else{
            return "Mobile Number Already exists !!!";
        }
    }

    @GetMapping("/getPendingMembership")
    public List<MemberShip> getPendingMembership() {
        return memberShipRepo.getPendingMembership("Not Active");
    }

    @GetMapping("/getMembers")
    public List<MemberShip> getMembers() {
        return memberShipRepo.getMembers();
    }

    @PostMapping("/approveMembership")
    public void approveMember(@RequestParam("mobileNumber") String mobileNumber,@RequestParam("memberId") String memberId)
    {
        memberShipRepo.approveMembership("Active",mobileNumber,memberId);
    }

    @PostMapping("/deleteBook")
    public String deleteBook(@RequestParam("bookId") String bookId, @RequestParam("noOfBooks") int noOfBooks)
    {
        Books books = booksRepo.findById(bookId).orElse(null);
        if(books==null)
        {
            return "Book not found";
        }
        if(books.getNoOfBooks()<noOfBooks)
        {
            return "Available book is less than you have entered !!!";
        }
        if(books.getNoOfBooks()==noOfBooks)
        {
            booksRepo.deleteById(bookId);
        }
        else
        {
            int t = books.getNoOfBooks();
            t=t-noOfBooks;
            books.setNoOfBooks(t);

            int h = books.getBooksLeft();
            h=h-noOfBooks;
            books.setBooksLeft(h);
            if(h<0)
            {
                return "Check-In the lost book";
            }
            booksRepo.save(books);
        }
        return "Book Removed";
    }
}
