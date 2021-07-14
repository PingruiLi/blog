package com.lpr.blog.service;

import com.lpr.blog.dao.TypeRepository;
import com.lpr.blog.entity.Type;
import com.lpr.blog.exception.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TypeServiceImpl implements TypeService{

    @Autowired
    private TypeRepository typeRepository;

    @Transactional
    @Override
    public Type saveType(Type type) {
        typeRepository.save(type);
        return type;
    }

    @Override
    public Type getType(Integer id) {
        Optional<Type> typeContainer = typeRepository.findById(id);
        if(!typeContainer.isPresent()) {
            throw new NotFoundException("分类不存在");
        }
        return typeContainer.get();
    }

    @Override
    public Type getTypeByName(String name) {
        return typeRepository.findByName(name);
    }

    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Type updateType(Integer id, Type type) {
        Optional<Type> typeContainer = typeRepository.findById(id);
        if(!typeContainer.isPresent()) {
            return null;
        }
        Type target = typeContainer.get();
        BeanUtils.copyProperties(type,target);
        return typeRepository.save(target);
    }

    @Transactional
    @Override
    public void deleteType(Integer id) {
        typeRepository.deleteById(id);
    }

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    @Override
    public List<Type> getTopTypes(int n) {
        List<String> properties = new ArrayList<>();
        properties.add("blogs.size");
        Sort sort = Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = PageRequest.of(0,n,sort);
        return typeRepository.findTopTypes(pageable);
    }
}
