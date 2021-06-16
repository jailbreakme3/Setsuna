/*
 * Copyright 2014 Mario Arias
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jailbreakme3.setsuna.partial

/**
 * Marker class to be used as the representation of a non-appliled parameter
 *
 * Created by IntelliJ IDEA.
 * @author Mario Arias
 * Date: 6/09/14
 * Time: 11:07
 */
class Partial<T>

fun <T> partial(): Partial<T> = Partial()