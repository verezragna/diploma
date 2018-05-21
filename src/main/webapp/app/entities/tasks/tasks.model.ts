import { BaseEntity } from './../../shared';

export class Tasks implements BaseEntity {
    constructor(
        public id?: number,
        public title?: string,
        public task?: string,
        public image_url?: string,
        public answer?: string,
        public group?: string,
        public user_tasksLogin?: string,
        public user_tasksId?: number,
    ) {
    }
}
