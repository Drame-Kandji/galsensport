package com.example.backend.controller;

import com.example.backend.dto.search.SearchResponse;
import com.example.backend.repository.EntrepriseRepository;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {
 private final UserRepository users; private final EntrepriseRepository companies; private final PostRepository posts;
 public SearchController(UserRepository users, EntrepriseRepository companies, PostRepository posts){this.users=users;this.companies=companies;this.posts=posts;}
 @GetMapping @PreAuthorize("isAuthenticated()")
 public ResponseEntity<SearchResponse> search(@RequestParam String q){
  String query=q==null?"":q.trim(); if(query.length()<2)return ResponseEntity.ok(new SearchResponse(java.util.List.of(),java.util.List.of(),java.util.List.of()));
  return ResponseEntity.ok(new SearchResponse(
   users.findTop10ByEmailContainingIgnoreCaseOrTelephoneContaining(query,query).stream().map(u->new SearchResponse.SearchUserResponse(u.getId(),u.getEmail(),u.getTelephone())).toList(),
   companies.findTop10ByNomEntrepriseContainingIgnoreCase(query).stream().map(c->new SearchResponse.SearchCompanyResponse(c.getUser().getId(),c.getNomEntreprise(),c.getDescription())).toList(),
   posts.findTop10ByContenuContainingIgnoreCaseOrderByCreatedAtDesc(query).stream().map(p->new SearchResponse.SearchPostResponse(p.getId(),p.getContenu(),p.getAuteur().getId(),p.getAuteur().getEmail())).toList()
  ));
 }
}
