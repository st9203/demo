package com.kim.myhome.controller;

import com.kim.myhome.model.Board;
import com.kim.myhome.repository.BoardRepository;
import com.kim.myhome.validator.BoardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    private  BoardValidator boardValidator;


    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 2) Pageable pageable, @RequestParam(required = false, defaultValue = "") String searchText) {

//        Page<Board> list = boardRepository.findAll(pageable);
        Page<Board> list = boardRepository.findByTitleContainingOrContentContaining(searchText, searchText , pageable);

        int startPage = Math.max(1 ,list.getPageable().getPageNumber() -4);
        int endPage = Math.min(list.getTotalPages(), list.getPageable().getPageNumber() +4);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("list" ,list);

       return "board/list";
    }

    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false)Long id){
        if(id== null){
            model.addAttribute("board",  new Board());
        }else{
            Board board =boardRepository.findById(id).orElse(null);
            System.out.println(board);
            model.addAttribute("board",board);
        }

        return "board/form";
    }

    @PostMapping("/form")
    public String submit(@Valid Board board, BindingResult bindingResult){
        boardValidator.validate(board,bindingResult);
        if (bindingResult.hasErrors()){
            return "board/form";
        }
        boardRepository.save(board);
        return "redirect:/board/list";
    }

    @DeleteMapping("/form/{id}/delete")
    public String delete(@PathVariable ("id")Long id){
        Board board = new Board();
        board.setId(id);
        System.out.println(id+"번 게시물 삭제.");
        boardRepository.delete(board);
        return "redirect:/board/list";
    }
}
