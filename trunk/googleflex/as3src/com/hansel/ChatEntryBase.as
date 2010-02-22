/**
 * Generated by Gas3 v2.1.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERWRITTEN EACH TIME YOU USE
 * THE GENERATOR. INSTEAD, EDIT THE INHERITED CLASS (ChatEntry.as).
 */

package com.hansel {

    import flash.utils.IDataInput;
    import flash.utils.IDataOutput;
    import flash.utils.IExternalizable;
    import org.granite.collections.IPersistentCollection;
    import org.granite.meta;

    use namespace meta;

    [Bindable]
    public class ChatEntryBase implements IExternalizable {

        private var __initialized:Boolean = true;
        private var __detachedState:String = null;

        public var _chatText:String;
        private var _key:String;
        public var _timestamp:Number;
        public var _user:UserEntry;

        meta function isInitialized(name:String = null):Boolean {
            if (!name)
                return __initialized;

            var property:* = this[name];
            return (
                (!(property is ChatEntry) || (property as ChatEntry).meta::isInitialized()) &&
                (!(property is IPersistentCollection) || (property as IPersistentCollection).isInitialized())
            );
        }

        public function set chatText(value:String):void {
            _chatText = value;
        }
        public function get chatText():String {
            return _chatText;
        }

        public function set timestamp(value:Number):void {
            _timestamp = value;
        }
        public function get timestamp():Number {
            return _timestamp;
        }

        public function set user(value:UserEntry):void {
            _user = value;
        }
        public function get user():UserEntry {
            return _user;
        }

        public function readExternal(input:IDataInput):void {
            __initialized = input.readObject() as Boolean;
            __detachedState = input.readObject() as String;
            if (meta::isInitialized()) {
                _chatText = input.readObject() as String;
                _key = input.readObject() as String;
                _timestamp = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
                _user = input.readObject() as UserEntry;
            }
            else {
                _key = input.readObject() as String;
            }
        }

        public function writeExternal(output:IDataOutput):void {
            output.writeObject(__initialized);
            output.writeObject(__detachedState);
            if (meta::isInitialized()) {
                output.writeObject(_chatText);
                output.writeObject(_key);
                output.writeObject(_timestamp);
                output.writeObject(_user);
            }
            else {
                output.writeObject(_key);
            }
        }
    }
}