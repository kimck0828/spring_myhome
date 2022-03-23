package com.example.myhome.controller;

import com.example.myhome.model.Board;
import com.example.myhome.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BoardApiController {
    
    @Autowired
    private BoardRepository boardRepository;
    
    @GetMapping("/boards")
    public List<Board> getAll(@RequestParam(required = false) String title,
                              @RequestParam(required = false) String content){
        if (StringUtils.isEmpty(title) && StringUtils.isEmpty(content)) {
            return boardRepository.findAll();
        } else {
            return boardRepository.findByTitleOrContent(title, content);
        }
    }
    
    @GetMapping("/boards/{id}")
    public Board getBoard(@PathVariable Long id) {
        return boardRepository.findById(id).orElse(null);
    }
    
    @PostMapping("/boards")
    public Board newBoard(@RequestBody Board board) {
        return boardRepository.save(board);
    }
    
    @PutMapping("/boards/{id}")
    public Board replaceBoard(@RequestBody Board board, @PathVariable Long id) {
        return boardRepository.findById(id)
                .map(nowBoard -> {
                    nowBoard.setTitle(board.getTitle());
                    nowBoard.setContent(board.getContent());
                    return boardRepository.save(nowBoard);
                })
                .orElseGet(() ->{
                    board.setId(id);
                    return boardRepository.save(board);
                });
    }
    
    @DeleteMapping("/boards/{id}")
    public void deleteBoard(@PathVariable Long id) {
        boardRepository.deleteById(id);
    }
    
}
