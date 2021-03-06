/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.devefx.validator.internal.engine.messageinterpolation.parser;

/**
 * @author Hardy Ferentschik
 */
public class InterpolationTermState implements ParserState {

    @Override
    public void start(TokenCollector tokenCollector) {
        throw new IllegalStateException("Parsing of message descriptor cannot start in this state");
    }

    @Override
    public void terminate(TokenCollector tokenCollector) throws MessageDescriptorFormatException {
        throw new MessageDescriptorFormatException("The message descriptor '" +
                tokenCollector.getOriginalMessageDescriptor() + "' contains an unbalanced meta character '" +
                TokenCollector.BEGIN_TERM + "' parameter."
        );
    }

    @Override
    public void handleNonMetaCharacter(char character, TokenCollector tokenCollector)
            throws MessageDescriptorFormatException {
        tokenCollector.appendToToken(character);
        tokenCollector.next();
    }

    @Override
    public void handleBeginTerm(char character, TokenCollector tokenCollector) throws MessageDescriptorFormatException {
        throw new MessageDescriptorFormatException("The message descriptor '" + tokenCollector.getOriginalMessageDescriptor() + "' has nested parameters.");
    }

    @Override
    public void handleEndTerm(char character, TokenCollector tokenCollector) throws MessageDescriptorFormatException {
        tokenCollector.appendToToken(character);
        tokenCollector.terminateToken();
        BeginState beginState = new BeginState();
        tokenCollector.transitionState(beginState);
        tokenCollector.next();
    }

    @Override
    public void handleEscapeCharacter(char character, TokenCollector tokenCollector)
            throws MessageDescriptorFormatException {
        tokenCollector.appendToToken(character);
        ParserState state = new EscapedState(this);
        tokenCollector.transitionState(state);
        tokenCollector.next();

    }

    @Override
    public void handleELDesignator(char character, TokenCollector tokenCollector)
            throws MessageDescriptorFormatException {
        tokenCollector.appendToToken(character);
        tokenCollector.next();
    }
}


