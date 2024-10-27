import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getStudentDetails } from 'app/entities/student-details/student-details.reducer';
import { getEntities as getSchools } from 'app/entities/school/school.reducer';
import { getEntities as getStudClasses } from 'app/entities/stud-class/stud-class.reducer';
import { createEntity, getEntity, reset, updateEntity } from './student.reducer';

export const StudentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const studentDetails = useAppSelector(state => state.studentDetails.entities);
  const schools = useAppSelector(state => state.school.entities);
  const studClasses = useAppSelector(state => state.studClass.entities);
  const studentEntity = useAppSelector(state => state.student.entity);
  const loading = useAppSelector(state => state.student.loading);
  const updating = useAppSelector(state => state.student.updating);
  const updateSuccess = useAppSelector(state => state.student.updateSuccess);

  const handleClose = () => {
    navigate('/student');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getStudentDetails({}));
    dispatch(getSchools({}));
    dispatch(getStudClasses({}));
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
      ...studentEntity,
      ...values,
      studentDetails: studentDetails.find(it => it.id.toString() === values.studentDetails?.toString()),
      school: schools.find(it => it.id.toString() === values.school?.toString()),
      studClasses: mapIdList(values.studClasses),
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
          ...studentEntity,
          studentDetails: studentEntity?.studentDetails?.id,
          school: studentEntity?.school?.id,
          studClasses: studentEntity?.studClasses?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="wednesdayApp.student.home.createOrEditLabel" data-cy="StudentCreateUpdateHeading">
            <Translate contentKey="wednesdayApp.student.home.createOrEditLabel">Create or edit a Student</Translate>
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
                  id="student-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('wednesdayApp.student.name')} id="student-name" name="name" data-cy="name" type="text" />
              <ValidatedField
                label={translate('wednesdayApp.student.lastName')}
                id="student-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
              />
              <ValidatedField
                id="student-studentDetails"
                name="studentDetails"
                data-cy="studentDetails"
                label={translate('wednesdayApp.student.studentDetails')}
                type="select"
              >
                <option value="" key="0" />
                {studentDetails
                  ? studentDetails.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="student-school"
                name="school"
                data-cy="school"
                label={translate('wednesdayApp.student.school')}
                type="select"
              >
                <option value="" key="0" />
                {schools
                  ? schools.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('wednesdayApp.student.studClasses')}
                id="student-studClasses"
                data-cy="studClasses"
                type="select"
                multiple
                name="studClasses"
              >
                <option value="" key="0" />
                {studClasses
                  ? studClasses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/student" replace color="info">
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

export default StudentUpdate;
