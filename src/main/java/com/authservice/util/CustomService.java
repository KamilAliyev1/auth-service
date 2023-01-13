package com.authservice.util;

import java.util.List;

public interface CustomService<SAVEDTO,UPDATEDTO> {

    Object findById(Long id) ;

    List<?> findAll();

    Object update(UPDATEDTO obj,Long id);

    boolean existsById(Long id) ;

    void deleteById(Long id);

    Object save(SAVEDTO obj);


}
