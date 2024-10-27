import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getStudents } from 'app/entities/student/student.reducer';
import { createEntity, getEntity, reset, updateEntity } from './stud-class.reducer';

export const StudClassUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const students = useAppSelector(state => state.student.entities);
  const studClassEntity = useAppSelector(state => state.studClass.entity);
  const loading = useAppSelector(state => state.studClass.loading);
  const updating = useAppSelector(state => state.studClass.updating);
  const updateSuccess = useAppSelector(state => state.studClass.updateSuccess);

  const handleClose = () => {
    navigate('/stud-class');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getStudents({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...studClassEntity,
      ...values,
      students: mapIdList(values.students),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...studClassEntity,
          students: studClassEntity?.students?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="wednesdayApp.studClass.home.createOrEditLabel" data-cy="StudClassCreateUpdateHeading">
            <Translate contentKey="wednesdayApp.studClass.home.createOrEditLabel">Create or edit a StudClass</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="stud-class-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('wednesdayApp.studClass.className')}
                id="stud-class-className"
                name="className"
                data-cy="className"
                type="text"
              />
              <ValidatedField
                label={translate('wednesdayApp.studClass.subject')}
                id="stud-class-subject"
                name="subject"
                data-cy="subject"
                type="text"
              />
              <ValidatedField
                label={translate('wednesdayApp.studClass.students')}
                id="stud-class-students"
                data-cy="students"
                type="select"
                multiple
                name="students"
              >
                <option value="" key="0" />
                {students
                  ? students.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/stud-class" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default StudClassUpdate;
