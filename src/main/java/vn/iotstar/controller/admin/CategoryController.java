package vn.iotstar.controller.admin;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import vn.iotstar.entity.Category;
import vn.iotstar.model.CategoryModel;
import vn.iotstar.service.CategoryService;

@Controller
@RequestMapping("admin/categories")
public class CategoryController {

	@Autowired
	CategoryService categoryService;
	
	@RequestMapping("")
	public String All(Model model) {
		List<Category> list = categoryService.findAll();
		
		model.addAttribute("list", list);
		return "admin/category/list";
	}
	
	@GetMapping("/add")
	public String Add(Model model) {
		CategoryModel cate = new CategoryModel();
		cate.setIsEdit(false);
		model.addAttribute("category", cate);
		return "admin/category/add";
	}
	
	@PostMapping("/save")
	public ModelAndView SaveOrUpdate(ModelMap model, @Valid @ModelAttribute("category") CategoryModel catemodel, BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("admin/category/add");
		}
		
		Category entity = new Category();
		
		// Copy from Model to Entity
		BeanUtils.copyProperties(catemodel, entity);
		
		// Call the Save function in Service
		categoryService.save(entity);
		
		// Return the message to the Message variable
		String message = "";
		if (catemodel.getIsEdit() == true) {
			message = "Category is Edited";
		}
		else {
			message = "Category is Saved";
		}
		
		model.addAttribute("message", message);
		
		// Redirect về URL Controller
		return new ModelAndView("forward:/admin/categories", model);
	}
	
	@GetMapping("/edit/{id}")
	public ModelAndView Edit(ModelMap model, @PathVariable("id") Long categoryid) {
		Optional<Category> optCategory = categoryService.findById(categoryid);
		CategoryModel cateModel = new CategoryModel();
		
		// Kiểm tra sự tồn tại của Category
		if (optCategory.isPresent()) {
			Category entity = optCategory.get();
			
			// Copy from Entity to CateModel
			BeanUtils.copyProperties(entity, cateModel);
			cateModel.setIsEdit(true);
			
			// Đẩy dữ liệu ra view
			model.addAttribute("category", cateModel);
			
			return new ModelAndView("admin/category/add", model);
		}
		
		model.addAttribute("message", "Category is not existed");
		return new ModelAndView("forward:/admin/categories", model);
	}
	
	@GetMapping("/delete/{id}")
	public ModelAndView Delete(ModelMap model, @PathVariable("id") Long categoryid) {
		categoryService.deleteById(categoryid);		
		model.addAttribute("message", "Category is deleted");
		return new ModelAndView("forward:/admin/categories", model);
	}
	
	@RequestMapping("/searchpaginated")
	public String search(ModelMap model, @RequestParam(name = "name", required = false) String name, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
	    int count = (int) categoryService.count();
	    int currentPage = page.orElse(1);
	    int pageSize = size.orElse(3);

	    Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("name"));
	    Page<Category> resultPage;

	    if (StringUtils.hasText(name)) {
	        resultPage = categoryService.findByNameContaining(name, pageable);
	        model.addAttribute("name", name);
	    } else {
	        resultPage = categoryService.findAll(pageable);
	    }

	    int totalPages = resultPage.getTotalPages();
	    if (totalPages > 0) {
	        int start = Math.max(1, currentPage - 2);
	        int end = Math.min(currentPage + 2, totalPages);

	        if (totalPages > count) {
	            if (end == totalPages) {
	                start = end - count;
	            } else if (start == 1) {
	                end = start + count;
	            }
	        }

	        List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
	            .boxed()
	            .collect(Collectors.toList());
	        model.addAttribute("pageNumbers", pageNumbers);
	    }

	    model.addAttribute("categoryPage", resultPage);
	    model.addAttribute("hasContent", resultPage.hasContent());
	    model.addAttribute("Content", resultPage.getContent());
	    List<Integer> Array = Arrays.asList(3, 5, 10, 15, 20);
	    model.addAttribute("Array", Array);
	    model.addAttribute("totalPages", totalPages);
	    return "admin/category/list";
	}

}
