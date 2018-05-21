import { BaseEntity } from './../../shared';

export class News implements BaseEntity {
    constructor(
        public id?: number,
        public title?: string,
        public image_url?: string,
        public description?: string,
        public text?: string,
        public group?: string,
        public user_newsLogin?: string,
        public user_newsId?: number,
    ) {
    }
}
