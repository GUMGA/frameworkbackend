/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.application;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author gyowannyqueiroz
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
public abstract class AbstractTest {

}
