package com.example.myhome.controller;

import com.example.myhome.model.Board;
import com.example.myhome.repository.BoardRepository;
import com.example.myhome.service.BoardService;
import com.example.myhome.validator.BoardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private BoardService boardService;
    
    @Autowired
    private BoardValidator boardValidator;
    
    /**
     * 掲示板一覧画面
     * @param model モデルインスタンス
     * @return 掲示板一覧ページ
     */
    @GetMapping("/list")
    public String list(Model model,@PageableDefault(size = 3) Pageable pageable,
                       @RequestParam(required = false, defaultValue = "") String searchText) {
        Page<Board> boards = boardRepository.findByTitleContainingOrContentContaining(searchText, searchText, pageable);

        int startPage = Math.max(1, boards.getPageable().getPageNumber() -4);
        int endPage = Math.min(boards.getPageable().getPageNumber() + 4, boards.getTotalPages());

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        
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
    public String form(Model model, @RequestParam(required = false) Long id, Authentication authentication) {
        Board board;
        if ( id == null) {
            board = new Board();
        } else {
            board = boardRepository.findById(id).orElse(new Board());
        }
        model.addAttribute("board", board);
        
        boolean boardOwnerIsMe = false;
        if ( id != null && board.getId() != null && authentication != null
                && authentication.getName().equals(board.getUser().getUsername()) ) {
            boardOwnerIsMe = true;
        }
        model.addAttribute("boardOwnerIsMe", boardOwnerIsMe);
        
        return "board/form";
    }

    /**
     * 掲示板入力画面（POST）
     * @param board 入力情報
     * @param bindingResult Validation結果
     * @param authentication 認証情報(パラメータ「Authentication」を追加すると、自動にSpringSecurity情報を取得できる）
     * @return 掲示板入力ページ
     */
    @PostMapping("/form")
    public String postForm(@ModelAttribute("board") @Validated Board board, BindingResult bindingResult, Authentication authentication) {
        boardValidator.validate(board, bindingResult);
        
        if ( bindingResult.hasErrors() ) {
            return "board/form";
        }

        String username = authentication.getName();
        boardService.save(username, board);
        
        return "redirect:/board/list";
    }
}
