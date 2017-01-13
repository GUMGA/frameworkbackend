/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.application;

import io.gumga.domain.domains.GumgaOi;
import io.gumga.domain.repository.GumgaCrudAndQueryNotOnlyTypedRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 *
 * @author wlademir
 */
public interface TaskRepository extends GumgaCrudAndQueryNotOnlyTypedRepository<Task, Long>{
    
    @Query("select obj from Task obj where obj.name like(?1) and obj.oi in(?2)")
    public List<Task>getTaskDescriptionAndOi(String description, List<GumgaOi>ois);
    
}
