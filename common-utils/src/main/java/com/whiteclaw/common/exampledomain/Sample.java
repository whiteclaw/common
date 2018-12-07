package com.whiteclaw.common.exampledomain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author whiteclaw
 * created on 2018-12-07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Sample implements Serializable {
    Integer id;
    String name;
}
