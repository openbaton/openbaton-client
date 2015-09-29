/*
 * Copyright (c) 2015 Fraunhofer FOKUS
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openbaton.common.vnfm_sdk;

import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.openbaton.catalogue.nfvo.Action;
import org.openbaton.catalogue.nfvo.messages.Interfaces.NFVMessage;
import org.openbaton.catalogue.nfvo.messages.OrVnfmErrorMessage;
import org.openbaton.catalogue.nfvo.messages.OrVnfmGenericMessage;
import org.openbaton.common.vnfm_sdk.exception.VnfmSdkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.io.Serializable;
import java.util.concurrent.Future;

/**
 * Created by lto on 23/09/15.
 */
public abstract class VnfmHelper {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Async
    public Future<VirtualNetworkFunctionRecord> grantLifecycleOperation(VirtualNetworkFunctionRecord vnfr) throws VnfmSdkException {
        NFVMessage response;
        try {
            response = sendAndReceive(Action.GRANT_OPERATION, vnfr);
        } catch (Exception e) {
            throw new VnfmSdkException("Not able to grant operation", e);
        }
        log.debug("" + response);
        if (response.getAction().ordinal() == Action.ERROR.ordinal()) {
            throw new VnfmSdkException("Not able to grant operation because: " + ((OrVnfmErrorMessage) response).getMessage() , ((OrVnfmErrorMessage) response).getVnfr());
        }
        OrVnfmGenericMessage orVnfmGenericMessage = (OrVnfmGenericMessage) response;
        return new AsyncResult<>(orVnfmGenericMessage.getVnfr());
    }

    @Async
    public Future<VirtualNetworkFunctionRecord> allocateResources(VirtualNetworkFunctionRecord vnfr) throws VnfmSdkException {
        NFVMessage response;
        try {
            response = sendAndReceive(Action.ALLOCATE_RESOURCES, vnfr);
        } catch (Exception e) {
            log.error("" + e.getMessage());
            throw new VnfmSdkException("Not able to allocate Resources", e);
        }
        if (response.getAction().ordinal() == Action.ERROR.ordinal()) {
            OrVnfmErrorMessage errorMessage = (OrVnfmErrorMessage) response;
            log.error(errorMessage.getMessage());
            throw new VnfmSdkException("Not able to allocate Resources because: " + errorMessage.getMessage() , errorMessage.getVnfr());
        }
        OrVnfmGenericMessage orVnfmGenericMessage = (OrVnfmGenericMessage) response;
        log.debug("Received from ALLOCATE: " + orVnfmGenericMessage.getVnfr());
        return new AsyncResult<>(orVnfmGenericMessage.getVnfr());
    }

    public abstract void sendMessageToQueue(String sendToQueueName, Serializable message);

    public abstract void sendToNfvo(NFVMessage nfvMessage);

    public abstract NFVMessage sendAndReceive(Action action, VirtualNetworkFunctionRecord virtualNetworkFunctionRecord) throws Exception;
}
