package edu.weeia.cynodesu.controllers;

import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ControllerPaginationUtil {
    public static <T> void decorateModel(Model model, Page<T> page) {
        int gap = page.getNumber() + 3;
        int begin = Math.max(1, gap - 5);
        int end = Math.min(begin + 6, page.getTotalPages());
        if (page.getTotalPages() == 0) {
            begin = 0;
        }

        // Add pagination range for display
        int totalPages = page.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("_page_begin", begin);
        model.addAttribute("_page_end", end);
    }
}