package com.myapp.domain;

import static com.myapp.domain.MyEntityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MyEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MyEntity.class);
        MyEntity myEntity1 = getMyEntitySample1();
        MyEntity myEntity2 = new MyEntity();
        assertThat(myEntity1).isNotEqualTo(myEntity2);

        myEntity2.setId(myEntity1.getId());
        assertThat(myEntity1).isEqualTo(myEntity2);

        myEntity2 = getMyEntitySample2();
        assertThat(myEntity1).isNotEqualTo(myEntity2);
    }
}
