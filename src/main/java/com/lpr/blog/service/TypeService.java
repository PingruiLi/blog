package com.lpr.blog.service;

import com.lpr.blog.entity.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

public interface TypeService {
    Type saveType(Type type);

    Type getType(Integer id);

    Type getTypeByName(String name);

    Page<Type> listType(Pageable pageable);

    List<Type> listType();

    Type updateType(Integer id, Type type);

    void deleteType(Integer Type);

    List<Type> getTopTypes(int n);
}
