/*
 * Copyright (C) 2019 The Android Open Source Project
 *
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

package com.android.ike.ikev2.message;

import com.android.ike.ikev2.exceptions.AuthenticationFailedException;
import com.android.ike.ikev2.exceptions.IkeException;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * IkeCertX509CertPayload represents a Certificate Payload carrying a DER-encoded X.509 certificate.
 *
 * <p>When sending multiple certificates, IKE library should put certificates in order starting with
 * the target certificate and ending with a certificate issued by the trust anchor. When receiving
 * an inbound packet, IKE library should take first certificate as the target certificate but treat
 * the rest unordered.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7296#section-3.6">RFC 7296, Internet Key Exchange
 *     Protocol Version 2 (IKEv2).
 */
public final class IkeCertX509CertPayload extends IkeCertPayload {
    public final X509Certificate certificate;

    protected IkeCertX509CertPayload(boolean critical, byte[] certData) throws IkeException {
        super(critical, CERTIFICATE_ENCODING_X509_CERT_SIGNATURE);
        try {
            CertificateFactory factory =
                    CertificateFactory.getInstance("X.509", IkeMessage.getSecurityProvider());
            certificate =
                    (X509Certificate)
                            factory.generateCertificate(new ByteArrayInputStream(certData));
            // Parsing InputStream error
            if (certificate == null) {
                throw new AuthenticationFailedException(
                        "No certificate parsed from received data.");
            }
            if (certificate.getEncoded().length < certData.length) {
                throw new AuthenticationFailedException("Unexpected trailing bytes.");
            }
        } catch (GeneralSecurityException e) {
            throw new AuthenticationFailedException(e);
        }
    }

    /**
     * Encode IkeCertX509CertPayload to ByteBuffer.
     *
     * @param nextPayload type of payload that follows this payload.
     * @param byteBuffer destination ByteBuffer that stores encoded payload.
     */
    @Override
    protected void encodeToByteBuffer(@PayloadType int nextPayload, ByteBuffer byteBuffer) {
        // TODO: Implement it.
        throw new UnsupportedOperationException(
                "It is not supported to encode a " + getTypeString());
    }

    /**
     * Get entire payload length.
     *
     * @return entire payload length.
     */
    @Override
    protected int getPayloadLength() {
        // TODO: Implement it.
        throw new UnsupportedOperationException(
                "It is not supported to get payload length of " + getTypeString());
    }

    /**
     * Return the payload type as a String.
     *
     * @return the payload type as a String.
     */
    @Override
    public String getTypeString() {
        return "Certificate Payload Carrying X.509 Certificate";
    }
}
