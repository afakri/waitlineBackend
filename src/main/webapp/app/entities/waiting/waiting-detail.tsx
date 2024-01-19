import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './waiting.reducer';

export const WaitingDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const waitingEntity = useAppSelector(state => state.waiting.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="waitingDetailsHeading">Waiting</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{waitingEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{waitingEntity.name}</dd>
          <dt>
            <span id="phone">Phone</span>
          </dt>
          <dd>{waitingEntity.phone}</dd>
          <dt>
            <span id="timeArrived">Time Arrived</span>
          </dt>
          <dd>
            {waitingEntity.timeArrived ? <TextFormat value={waitingEntity.timeArrived} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="timeSummoned">Time Summoned</span>
          </dt>
          <dd>
            {waitingEntity.timeSummoned ? <TextFormat value={waitingEntity.timeSummoned} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="timeDone">Time Done</span>
          </dt>
          <dd>{waitingEntity.timeDone ? <TextFormat value={waitingEntity.timeDone} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>Queue</dt>
          <dd>{waitingEntity.queue ? waitingEntity.queue.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/waiting" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/waiting/${waitingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default WaitingDetail;
