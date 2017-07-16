/*
* Copyright 2017, Julius Krah
* by the @authors tag. See the LICENCE in the distribution for a
* full listing of individual contributors.
*
* Licensed under the GNU General Public License, Version 3 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.gnu.org/licenses/
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.juliuskrah;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

@XmlRootElement
public class Resource {
    private Long id;
    private String description;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    public Resource() {
    }

    public Resource(Long id, String description, LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.description = description;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getModifiedTime() {
        return this.modifiedTime;
    }

    public void setModifiedTime(LocalDateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}