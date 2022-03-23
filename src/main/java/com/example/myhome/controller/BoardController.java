package com.example.myhome.controller;

import com.example.myhome.model.Board;
import com.example.myhome.repository.BoardRepository;
import com.example.myhome.validator.BoardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {
    
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardValidator boardValidator;
    
    /**
     * 掲示板一覧画面
     * @param model モデルインスタンス
     * @return 掲示板一覧ページ
     */
    @GetMapping("/list")
    public String list(Model model) {
        List<Board> boards = boardRepository.findAll();
        model.addAttribute("boards", boards);
        return "board/list";
    }

    /**
     * 掲示板入力画面（GET）
     * @param model モデルインスタンス
     * @param id 掲示板ID
     * @return 掲示板入力ページ
     */
    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false) Long id) {
        Board board;
        if ( id == null) {
            board = new Board();
        } else {
            board = boardRepository.findById(id).orElse(null);
        }
        model.addAttribute("board", board);
        return "board/form";
    }

    /**
     * 掲示板入力画面（POST）
     * @param board 入力情報
     * @param bindingResult Validation結果
     * @return 掲示板入力ページ
     */
    @PostMapping("/form")
    public String formSubmit(@ModelAttribute("board") @Validated Board board, BindingResult bindingResult) {
        boardValidator.validate(board, bindingResult);
        
        if ( bindingResult.hasErrors() ) {
            return "board/form";
        }
        boardRepository.save(board);
        return "redirect:/board/list";
    }
}
