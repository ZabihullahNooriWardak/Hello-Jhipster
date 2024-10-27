import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/my-entity">
        <Translate contentKey="global.menu.entities.myEntity" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/student">
        <Translate contentKey="global.menu.entities.student" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/student-details">
        <Translate contentKey="global.menu.entities.studentDetails" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/stud-class">
        <Translate contentKey="global.menu.entities.studClass" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/school">
        <Translate contentKey="global.menu.entities.school" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
