package com.bookhub.controller;

//• POST /api/loans
//• GET /api/loans/my
//• GET /api/loans (LIBRARIAN)
//• PUT /api/loans/{id}/return (LIBRARIAN)

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/loans")
public class EmpruntController {
}
