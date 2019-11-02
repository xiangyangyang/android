package com.chai.xiangyang.gridstickerheaderlist.data


class DataBaseHelper {
//    private val realm: Realm = Realm.getDefaultInstance()
//
//    fun <T : RealmObject> getByPrimaryKey(columnName: String, primaryKey: Int, clazz: Class<T>): Maybe<T> {
//        return Maybe.create<T> { emitter ->
//            try {
//                val entity = realm.where(clazz).equalTo(columnName, primaryKey).findFirst()
//                if (entity != null)
//                    emitter.onSuccess(entity)
//                else emitter.onComplete()
//            } catch (t: Throwable) {
//                emitter.onError(getDbException(t))
//            } finally {
//                realm.close()
//            }
//        }
//    }
//
//    fun <T : RealmObject> findAll(clazz: Class<T>): Single<List<T>> {
//        return Single.create<List<T>> { emitter ->
//            try {
//                val results = realm.where(clazz).findAll()
//                emitter.onSuccess(results)
//            } catch (t: Throwable) {
//                emitter.onError(getDbException(t))
//            } finally {
//                realm.close()
//            }
//        }
//    }
//
//    fun <T : RealmObject> insert(entity: T): Completable {
//        return Completable.create { emitter ->
//            try {
//                realm.executeTransaction { realm -> realm.insert(entity) }
//            } catch (t: Throwable) {
//                emitter.onError(getDbException(t))
//            } finally {
//                realm.close()
//            }
//        }
//    }

    private fun getDbException(t: Throwable): Exception {
        return Exception("DB エラーが発生しました。", t)
    }
}