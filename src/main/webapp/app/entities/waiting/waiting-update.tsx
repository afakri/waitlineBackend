import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IQueue } from 'app/shared/model/queue.model';
import { getEntities as getQueues } from 'app/entities/queue/queue.reducer';
import { IWaiting } from 'app/shared/model/waiting.model';
import { getEntity, updateEntity, createEntity, reset } from './waiting.reducer';

export const WaitingUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const queues = useAppSelector(state => state.queue.entities);
  const waitingEntity = useAppSelector(state => state.waiting.entity);
  const loading = useAppSelector(state => state.waiting.loading);
  const updating = useAppSelector(state => state.waiting.updating);
  const updateSuccess = useAppSelector(state => state.waiting.updateSuccess);

  const handleClose = () => {
    navigate('/waiting' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getQueues({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.timeArrived = convertDateTimeToServer(values.timeArrived);
    values.timeSummoned = convertDateTimeToServer(values.timeSummoned);
    values.timeDone = convertDateTimeToServer(values.timeDone);

    const entity = {
      ...waitingEntity,
      ...values,
      queue: queues.find(it => it.id.toString() === values.queue.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          timeArrived: displayDefaultDateTime(),
          timeSummoned: displayDefaultDateTime(),
          timeDone: displayDefaultDateTime(),
        }
      : {
          ...waitingEntity,
          timeArrived: convertDateTimeFromServer(waitingEntity.timeArrived),
          timeSummoned: convertDateTimeFromServer(waitingEntity.timeSummoned),
          timeDone: convertDateTimeFromServer(waitingEntity.timeDone),
          queue: waitingEntity?.queue?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="waitlineBackendApp.waiting.home.createOrEditLabel" data-cy="WaitingCreateUpdateHeading">
            Create or edit a Waiting
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="waiting-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Name"
                id="waiting-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Phone"
                id="waiting-phone"
                name="phone"
                data-cy="phone"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Time Arrived"
                id="waiting-timeArrived"
                name="timeArrived"
                data-cy="timeArrived"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Time Summoned"
                id="waiting-timeSummoned"
                name="timeSummoned"
                data-cy="timeSummoned"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Time Done"
                id="waiting-timeDone"
                name="timeDone"
                data-cy="timeDone"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="waiting-queue" name="queue" data-cy="queue" label="Queue" type="select">
                <option value="" key="0" />
                {queues
                  ? queues.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/waiting" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default WaitingUpdate;
