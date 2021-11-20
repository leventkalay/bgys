import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Onay } from 'app/entities/enumerations/onay.model';
import { ITehdit, Tehdit } from '../tehdit.model';

import { TehditService } from './tehdit.service';

describe('Tehdit Service', () => {
  let service: TehditService;
  let httpMock: HttpTestingController;
  let elemDefault: ITehdit;
  let expectedResult: ITehdit | ITehdit[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TehditService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      adi: 'AAAAAAA',
      aciklama: 'AAAAAAA',
      onayDurumu: Onay.ONAYLANMADI,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Tehdit', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Tehdit()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Tehdit', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          adi: 'BBBBBB',
          aciklama: 'BBBBBB',
          onayDurumu: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Tehdit', () => {
      const patchObject = Object.assign(
        {
          aciklama: 'BBBBBB',
          onayDurumu: 'BBBBBB',
        },
        new Tehdit()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Tehdit', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          adi: 'BBBBBB',
          aciklama: 'BBBBBB',
          onayDurumu: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Tehdit', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTehditToCollectionIfMissing', () => {
      it('should add a Tehdit to an empty array', () => {
        const tehdit: ITehdit = { id: 123 };
        expectedResult = service.addTehditToCollectionIfMissing([], tehdit);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tehdit);
      });

      it('should not add a Tehdit to an array that contains it', () => {
        const tehdit: ITehdit = { id: 123 };
        const tehditCollection: ITehdit[] = [
          {
            ...tehdit,
          },
          { id: 456 },
        ];
        expectedResult = service.addTehditToCollectionIfMissing(tehditCollection, tehdit);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Tehdit to an array that doesn't contain it", () => {
        const tehdit: ITehdit = { id: 123 };
        const tehditCollection: ITehdit[] = [{ id: 456 }];
        expectedResult = service.addTehditToCollectionIfMissing(tehditCollection, tehdit);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tehdit);
      });

      it('should add only unique Tehdit to an array', () => {
        const tehditArray: ITehdit[] = [{ id: 123 }, { id: 456 }, { id: 56944 }];
        const tehditCollection: ITehdit[] = [{ id: 123 }];
        expectedResult = service.addTehditToCollectionIfMissing(tehditCollection, ...tehditArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tehdit: ITehdit = { id: 123 };
        const tehdit2: ITehdit = { id: 456 };
        expectedResult = service.addTehditToCollectionIfMissing([], tehdit, tehdit2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tehdit);
        expect(expectedResult).toContain(tehdit2);
      });

      it('should accept null and undefined values', () => {
        const tehdit: ITehdit = { id: 123 };
        expectedResult = service.addTehditToCollectionIfMissing([], null, tehdit, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tehdit);
      });

      it('should return initial array if no Tehdit is added', () => {
        const tehditCollection: ITehdit[] = [{ id: 123 }];
        expectedResult = service.addTehditToCollectionIfMissing(tehditCollection, undefined, null);
        expect(expectedResult).toEqual(tehditCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
