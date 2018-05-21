import { BaseEntity, User } from './../../shared';

export class Messages implements BaseEntity {
    constructor(
        public id?: number,
        public from?: number,
        public to?: number,
        public message?: string,
        public user_messages?: User[],
    ) {
    }
}
