import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './my-entity.reducer';

export const MyEntityDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const myEntityEntity = useAppSelector(state => state.myEntity.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="myEntityDetailsHeading">
          <Translate contentKey="wednesdayApp.myEntity.detail.title">MyEntity</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{myEntityEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="wednesdayApp.myEntity.name">Name</Translate>
            </span>
          </dt>
          <dd>{myEntityEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="wednesdayApp.myEntity.description">Description</Translate>
            </span>
          </dt>
          <dd>{myEntityEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/my-entity" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/my-entity/${myEntityEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MyEntityDetail;
