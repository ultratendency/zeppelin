/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.zeppelin.notebook.socket;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import org.apache.zeppelin.common.JsonSerializable;

/**
 * Zeppelin websocket massage template class.
 */
public class Message implements JsonSerializable {
  /**
   * Representation of event type.
   */
  public enum OP {
    // [c-s] load note for home screen
    GET_HOME_NOTE,
    // [c-s] client load note
    // @param id note id
    GET_NOTE,
    // [s-c] note info
    // @param note serialized Note object
    NOTE,
    // [s-c] paragraph info
    // @param paragraph serialized paragraph object
    PARAGRAPH,
    // [s-c] progress update
    // @param id paragraph id
    // @param progress percentage progress
    PROGRESS,
    // [c-s] create new notebook
    NEW_NOTE,
    // [c-s] delete notebook
    // @param id note id
    DEL_NOTE,
    REMOVE_FOLDER,
    MOVE_NOTE_TO_TRASH,
    MOVE_FOLDER_TO_TRASH,
    RESTORE_FOLDER,
    RESTORE_NOTE,
    RESTORE_ALL,
    EMPTY_TRASH,
    // [c-s] clone new notebook
    // @param id id of note to clone
    // @param name name for the cloned note
    CLONE_NOTE,
    // [c-s] import notebook
    // @param object notebook
    IMPORT_NOTE,
    NOTE_UPDATE,
    NOTE_RENAME,
    // [c-s] update personalized mode (boolean)
    // @param note id and boolean personalized mode value
    UPDATE_PERSONALIZED_MODE,
    FOLDER_RENAME,
    // [c-s] run paragraph
    // @param id paragraph id
    // @param paragraph paragraph content.ie. script
    // @param config paragraph config
    // @param params paragraph params
    RUN_PARAGRAPH,
    // [c-s] commit paragraph
    // @param id paragraph id
    // @param title paragraph title
    // @param paragraph paragraph content.ie. script
    // @param config paragraph config
    // @param params paragraph params
    COMMIT_PARAGRAPH,
    // [c-s] cancel paragraph run
    // @param id paragraph id
    CANCEL_PARAGRAPH,
    // [c-s] move paragraph order
    // @param id paragraph id
    // @param index index the paragraph want to go
    MOVE_PARAGRAPH,
    // [c-s] create new paragraph below current paragraph
    // @param target index
    INSERT_PARAGRAPH,
    // [c-s] create new para below current para as a copy of current para
    // @param target index
    // @param title paragraph title
    // @param paragraph paragraph content.ie. script
    // @param config paragraph config
    // @param params paragraph params
    COPY_PARAGRAPH,
    // [c-s] ask paragraph editor setting
    // @param magic magic keyword written in paragraph
    // ex) spark.spark or spark
    EDITOR_SETTING,
    // [c-s] ask completion candidates
    // @param id
    // @param buf current code
    // @param cursor cursor position in code
    COMPLETION,
    // [s-c] send back completion candidates list
    // @param id
    // @param completions list of string
    COMPLETION_LIST,
    // [c-s] ask list of note
    LIST_NOTES,
    // [c-s] reload notes from repo
    RELOAD_NOTES_FROM_REPO,
    // [s-c] list of note infos
    // @param notes serialized List<NoteInfo> object
    NOTES_INFO,
    PARAGRAPH_REMOVE,
    // [c-s] clear output of paragraph
    PARAGRAPH_CLEAR_OUTPUT,
    // [c-s] clear output of all paragraphs
    PARAGRAPH_CLEAR_ALL_OUTPUT,
    // [s-c] append output
    PARAGRAPH_APPEND_OUTPUT,
    // [s-c] update (replace) output
    PARAGRAPH_UPDATE_OUTPUT,
    PING,
    AUTH_INFO,
    // [s-c] add/update angular object
    ANGULAR_OBJECT_UPDATE,
    // [s-c] add angular object del
    ANGULAR_OBJECT_REMOVE,
    // [c-s] angular object value updated,
    ANGULAR_OBJECT_UPDATED,
    // [c-s] angular object updated from AngularJS z object
    ANGULAR_OBJECT_CLIENT_BIND,
    // [c-s] angular object unbind from AngularJS z object
    ANGULAR_OBJECT_CLIENT_UNBIND,
    // [c-s] ask all key/value pairs of configurations
    LIST_CONFIGURATIONS,
    // [s-c] all key/value pairs of configurations
    // @param settings serialized Map<String, String> object
    CONFIGURATIONS_INFO,
    // [c-s] checkpoint note to storage repository
    // @param noteId
    // @param checkpointName
    CHECKPOINT_NOTE,
    // [c-s] list revision history of the notebook
    // @param noteId
    LIST_REVISION_HISTORY,
    // [c-s] get certain revision of note
    // @param noteId
    // @param revisionId
    NOTE_REVISION,
    // [c-s] set current notebook head to this revision
    // @param noteId
    // @param revisionId
    SET_NOTE_REVISION,
    // [c-s] get certain revision of note for compare
    // @param noteId
    // @param revisionId
    // @param position
    NOTE_REVISION_FOR_COMPARE,
    // [s-c] append output
    APP_APPEND_OUTPUT,
    // [s-c] update (replace) output
    APP_UPDATE_OUTPUT,
    // [s-c] on app load
    APP_LOAD,
    // [s-c] on app status change
    APP_STATUS_CHANGE,
    // [c-s] get note job management information
    LIST_NOTE_JOBS,
    // [c-s] get job management information for until unixtime
    LIST_UPDATE_NOTE_JOBS,
    // [c-s] unsubscribe job information for job management
    // @param unixTime
    UNSUBSCRIBE_UPDATE_NOTE_JOBS,
    // [c-s] get interpreter bindings
    // @param noteId
    GET_INTERPRETER_BINDINGS,
    // [c-s] save interpreter bindings
    // @param noteId
    // @param selectedSettingIds
    SAVE_INTERPRETER_BINDINGS,
    // [s-c] interpreter bindings
    INTERPRETER_BINDINGS,
    // [c-s] get interpreter settings
    GET_INTERPRETER_SETTINGS,
    // [s-c] interpreter settings
    INTERPRETER_SETTINGS,
    // [s-c] error information to be sent
    ERROR_INFO,
    // [s-c] error information to be sent
    SESSION_LOGOUT,
    // [s-c] Change websocket to watcher mode.
    WATCHER,
    // [s-c] paragraph is added
    PARAGRAPH_ADDED,
    // [s-c] paragraph deleted
    PARAGRAPH_REMOVED,
    // [s-c] paragraph moved
    PARAGRAPH_MOVED,
    // [s-c] paragraph updated(name, config)
    NOTE_UPDATED,
    // [c-s] run all paragraphs
    RUN_ALL_PARAGRAPHS,
    // [c-s] paragraph was executed by spell
    PARAGRAPH_EXECUTED_BY_SPELL,
    // [s-c] run paragraph using spell
    RUN_PARAGRAPH_USING_SPELL,
    // [s-c] paragraph runtime infos
    PARAS_INFO,
    // save note forms
    SAVE_NOTE_FORMS,
    // remove note forms
    REMOVE_NOTE_FORMS
  }

  private static final Gson gson = new Gson();
  public static final Message EMPTY = new Message(null);
  
  public OP op;
  public Map<String, Object> data = new HashMap<>();
  public String ticket = "anonymous";
  public String principal = "anonymous";
  public String roles = "";

  public Message(OP op) {
    this.op = op;
  }

  public Message put(String k, Object v) {
    data.put(k, v);
    return this;
  }

  public Object get(String k) {
    return data.get(k);
  }

  public <T> T getType(String key) {
    return (T) data.get(key);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Message{");
    sb.append("data=").append(data);
    sb.append(", op=").append(op);
    sb.append('}');
    return sb.toString();
  }

  public String toJson() {
    return gson.toJson(this);
  }

  public static Message fromJson(String json) {
    return gson.fromJson(json, Message.class);
  }
}
