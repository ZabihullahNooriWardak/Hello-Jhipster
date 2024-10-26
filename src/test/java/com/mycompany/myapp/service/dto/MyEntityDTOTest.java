package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MyEntityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MyEntityDTO.class);
        MyEntityDTO myEntityDTO1 = new MyEntityDTO();
        myEntityDTO1.setId(1L);
        MyEntityDTO myEntityDTO2 = new MyEntityDTO();
        assertThat(myEntityDTO1).isNotEqualTo(myEntityDTO2);
        myEntityDTO2.setId(myEntityDTO1.getId());
        assertThat(myEntityDTO1).isEqualTo(myEntityDTO2);
        myEntityDTO2.setId(2L);
        assertThat(myEntityDTO1).isNotEqualTo(myEntityDTO2);
        myEntityDTO1.setId(null);
        assertThat(myEntityDTO1).isNotEqualTo(myEntityDTO2);
    }
}
