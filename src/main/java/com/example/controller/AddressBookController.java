package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.common.BaseContext;
import com.example.common.R;
import com.example.entity.AddressBook;
import com.example.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    @PutMapping("/default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        log.info("addressBook:{}", addressBook);
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        //先全部置0，再置1
        updateWrapper.set(AddressBook::getIsDefault, 0);
        addressBookService.update(updateWrapper);

        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

    @GetMapping("/{id}")
    public R get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return R.success(addressBook);
        } else {
            return R.error("找不到该对象");
        }
    }

    @GetMapping("/default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        if(addressBook != null) {
            return R.success(addressBook);
        } else {
            return R.error("没有找到该对象");
        }
    }

    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook:{}", addressBook);

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(addressBook.getUserId() != null, AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        return R.success(addressBookService.list(queryWrapper));
    }

    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook) {
        log.info("updating AddressBook...{}", addressBook);
        addressBookService.updateById(addressBook);
        return R.success("删除成功");
    }

}
